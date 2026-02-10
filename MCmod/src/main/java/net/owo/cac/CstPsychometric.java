package net.owo.cac;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	// Current
	public static double entropy_bin = 0;
	public static double[] likelihood_bin; // [grid]
	public static double[][] probability_bin; // [diff][grid]

	// Expected
	public static double[] expected_P_bin; // [diff]
	public static double[][][] expected_L_bin; // [win/lose][diff][grid]
	public static double[][] expected_H_bin; // [win/lose][diff]
	public static double[] EIG_bin; // [diff]
	public static double rho_best = 0;

	// parameter list
	public static int GRIDSIZE = 10000;
	public static int RHOSIZE = 20;
	public static double[] M; // [grid]
	public static double[] W; // [grid]
	public static double[] GAMMA; //[grid]
	public static double[] LAMBDA; //[grid]

	// grid of difficulty
	public static double[] RHO; // [diff]

	// Terminal Rule
	public static double[] IG_last = new double[3];
	public static double IG_THRESHOLD = 0.1;


	// Debug
	public static void debugValue() {
		initBin(0);
		updateTrialBefore();
		for (int r=0; r<RHOSIZE; r++) {
			System.out.println(probability_bin[r][0]);
		}
		System.out.println(likelihood_bin[0]);
		System.out.println(entropy_bin);
		for (int r=0; r<RHOSIZE; r++) {
			System.out.println(expected_P_bin[r]);
			System.out.println(expected_L_bin[0][r][0]);
			System.out.println(expected_H_bin[0][r]);
		}
	}

	// usage
	// initialize
	public static void initBin(int func_type) {
		GRIDSIZE = getGridShape(0) * getGridShape(1) * getGridShape(2) * getGridShape(3);
		initBinParam();
		initBinRho();
		initBinL();
		initBinP(func_type);
		initPrior();
	}
	// Repeat (Before trial)
	public static void updateTrialBefore() {
		expected_P_bin = calExpectedProbability(probability_bin, likelihood_bin);
		expected_L_bin = calExpectedLikelihood(probability_bin, likelihood_bin, expected_P_bin);
		expected_H_bin = calExpectedEntropy(expected_L_bin);
		EIG_bin = calEIG(entropy_bin, expected_P_bin, expected_H_bin);
		rho_best = RHO[argmax(EIG_bin)];
		CacModVariables.Dat_difficulty = rho_best;
	}
	// Repeat (After trial)
	public static void updateTrialAfter() {
		int winlose = (int) CacModVariables.Dat_trial_winlose;
		int rho_curr = getRhoIndex(CacModVariables.Dat_difficulty);
		if (winlose == 1) {
			updateIG(entropy_bin, expected_H_bin[0][rho_curr]);
			likelihood_bin = expected_L_bin[0][rho_curr].clone();
			entropy_bin = expected_H_bin[0][rho_curr];
		} else if (winlose == 0) {
			updateIG(entropy_bin, expected_H_bin[1][rho_curr]);
			likelihood_bin = expected_L_bin[1][rho_curr].clone();
			entropy_bin = expected_H_bin[1][rho_curr];
		}
		checkTerminate();
	}
	// Terminate
	public static void checkTerminate() {
		int stack = 0;
		for (int i=0; i<3; i++) {
			if ((IG_last[i] != 0) && (IG_last[i]<IG_THRESHOLD)) {
				stack += 1;
			}
		}
		if (stack >= 3) {
			CacModVariables.Exp_trial_total = 1;
		} else {
			CacModVariables.Exp_trial_total = 5;
		}
	}


	// init functions
	public static void initBinParam() {
		JsonArray param_min = CacModVariables.Psy_bin_param_min;
		JsonArray param_max = CacModVariables.Psy_bin_param_max;
		JsonArray param_step = CacModVariables.Psy_bin_param_step;
		
		for (int i=0; i<4; i++) {
			double P[] = new double[getGridShape(i)];
			double p_min = param_min.get(i).getAsDouble();
			double p_max = param_max.get(i).getAsDouble();
			double p_step = param_step.get(i).getAsDouble();
			for (int k=0; k<P.length; k++) {
				P[k] = Math.round((p_min + k*p_step)*1000) / 1000.0;
			}
			if (i==0) {
				M = P.clone();
			} else if (i==1) {
				W = P.clone();
			} else if (i==2) {
				GAMMA = P.clone();
			} else if (i==3) {
				LAMBDA = P.clone();
			}
		}
	}
	public static void initBinRho() {
		// rho : [0.9, 1.1)_0.01
		RHO = new double[RHOSIZE];
		double RHO_min = 0.90;
		double RHO_step = 0.01;
		for (int r=0; r<RHOSIZE; r++) {
			RHO[r] = Math.round((RHO_min + r*RHO_step)*1000) / 1000.0;
		}
	}
	public static void initBinL() {
		likelihood_bin = new double[GRIDSIZE];
	}
	public static void initBinP(int func_type) {
		probability_bin = new double[RHOSIZE][GRIDSIZE];
		double P = 0;
		int[] IDX = new int[4];
		for (int k=0; k<GRIDSIZE; k++) {
			IDX[0] = reshapeIndex(k, 0);
			IDX[1] = reshapeIndex(k, 1);
			IDX[2] = reshapeIndex(k, 2);
			IDX[3] = reshapeIndex(k, 3);
			for (int r=0; r<RHOSIZE; r++) {
				P = calPSI(func_type, RHO[r], M[IDX[0]], W[IDX[1]], GAMMA[IDX[2]], LAMBDA[IDX[3]]);
				probability_bin[r][k] = P;
			}
		}
	}
	public static void initPrior() {
		// json get
		JsonArray param_prior = CacModVariables.Psy_bin_param_prior;
		int pm = param_prior.get(0).getAsInt();
		int pw = param_prior.get(1).getAsInt();
		int pgamma = param_prior.get(2).getAsInt();
		int plambda = param_prior.get(3).getAsInt();
		
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D_sq = 0;
		for (int sm=0; sm<getGridShape(0); sm++) {
			for (int sw=0; sw<getGridShape(1); sw++) {
				for (int sgamma=0; sgamma<getGridShape(2); sgamma++) {
					for (int slambda=0; slambda<getGridShape(3); slambda++) {
						D_sq = Math.pow(sm-pm, 2) + Math.pow(sw-pw, 2) + Math.pow(sgamma-pgamma, 2) + Math.pow(slambda-plambda, 2);
						likelihood_bin[flattenIndex(sm, sw, sgamma, slambda)] = Math.log(0.5 + 0.5*Math.exp(-D_sq/4));
					}
				}
			}
		}
		likelihood_bin = normL(likelihood_bin.clone());
		entropy_bin = calEntropy(likelihood_bin);
	}


	// Recording Data on log file
	public static void recHistory() {
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_task = new JsonObject();
		JsonObject obj_history = new JsonObject();
		JsonObject obj_trial = new JsonObject();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_fitting));
			StringBuilder jsonstringbuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonstringbuilder.append(line);
			}
			bufferedReader.close();
			obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
			obj_cac = obj_file.get("cac").getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		obj_task = obj_cac.get((CacModVariables.Psy_task + "_" + CacModVariables.Psy_method + "_" + CacModVariables.Psy_function)).getAsJsonObject();
		obj_history = obj_task.get("history").getAsJsonObject();
		obj_trial.addProperty("difficulty", CacModVariables.Dat_difficulty);
		obj_trial.addProperty("entropy", entropy_bin);
		obj_trial.add("EIGs", getEIGs());
		obj_trial.add("param_best", getBestParam());
		obj_trial.add("likelihood", getLikelihood());
		obj_history.add(("trial" + "_" + CacModVariables.Exp_trial), obj_trial);
		com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fileWriter = new FileWriter(CacModVariables.Log_fitting);
			fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
			fileWriter.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	public static void recFinal() {
		Gson GS = new Gson();
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_task = new JsonObject();
		JsonObject obj_final = new JsonObject();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_fitting));
			StringBuilder jsonstringbuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonstringbuilder.append(line);
			}
			bufferedReader.close();
			obj_file = GS.fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
			obj_cac = obj_file.get("cac").getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		obj_task = obj_cac.get((CacModVariables.Psy_task + "_" + CacModVariables.Psy_method + "_" + CacModVariables.Psy_function)).getAsJsonObject();
		obj_final = obj_task.get("final").getAsJsonObject();
		obj_final.addProperty("entropy", entropy_bin);
		obj_final.add("param_best", getBestParam());
		obj_final.add("likelihood", getLikelihood());
		Gson mainGSONBuilderVariable = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fileWriter = new FileWriter(CacModVariables.Log_fitting);
			fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
			fileWriter.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	
	// Calculate the psychometric function (CDF)
	public static double calPSI(int func_type, double rho, double m, double w, double gamma, double lambda) {
		double F = 0;
		if (func_type == 0) { // Logistic
			F = funcLogistic(rho, m, w);
		}
		return gamma + (1-gamma-lambda)*F;
	}
	public static double funcLogistic(double rho, double m, double w) {
		double y = 1 / (1 + Math.pow(9, (rho-m)/w));
		return y;
	}
	
	
	// Index rearrange
	public static int flattenIndex(int sm, int sw, int sgamma, int slambda) {
		int GA = getGridShape(0);
		int GB = getGridShape(1);
		int GC = getGridShape(2);
		int GD = getGridShape(3);
		return sm*GB*GC*GD + sw*GC*GD + sgamma*GD + slambda;
	}
	public static int reshapeIndex(int sgrid, int idx_param) {
		int idx = 0;
		int GA = getGridShape(0);
		int GB = getGridShape(1);
		int GC = getGridShape(2);
		int GD = getGridShape(3);
		if (idx_param == 3) { // lambda
			idx = sgrid % GD;
		} else if (idx_param == 2) { // gamma
			idx = (sgrid / GD) % GC;
		} else if (idx_param == 1) { // w
			idx = ((sgrid / GD) / GC) % GB;
		} else if (idx_param == 0) { // m
			idx = ((sgrid / GD) / GC) / GB;
		}
		return idx;
	}
	public static int getRhoIndex(double rho) {
		int rho_id = -1;
		for (int r=0; r<RHOSIZE; r++) {
			if (RHO[r] == rho) {
				rho_id = r;
			}
		}
		return rho_id;
	}
	

	// Grid Normalize
	public static double[] normL(double[] L_hat) {
		// L_hat is unnormalized likelihood
		double[] L = new double[L_hat.length];
		double Z = 0;
		for (int k=0; k<L_hat.length; k++) {
			Z += Math.exp(L_hat[k]);
		}
		Z = Math.log(Z);
		for (int k=0; k<L.length; k++) {
			L[k] = L_hat[k] - Z;
		}
		return L;
	}


	// Update IG
	public static void updateIG(double H, double H_next) {
		boolean isfull = true;
		double IG = H - H_next;
		for (int i=0; i<3; i++) {
			if ((IG_last[i] == 0) && (isfull)) {
				IG_last[i] = IG;
				isfull = false;
			}
		}
		if (isfull) {
			IG_last[0] = IG_last[1];
			IG_last[1] = IG_last[2];
			IG_last[2] = IG;
		}
	}
	

	// Expected Components
	// expected win probability
	public static double[] calExpectedProbability(double[][] P, double[] L) {
		double[] ExP = new double[RHOSIZE];
		double ExP_temp = 0;
		for (int r=0; r<RHOSIZE; r++) {
			ExP_temp = 0;
			for (int k=0; k<GRIDSIZE; k++) {
				ExP_temp += P[r][k] * Math.exp(L[k]);
			}
			ExP[r] = ExP_temp;
		}
		return ExP;
	}
	// expected likelihood
	public static double[][][] calExpectedLikelihood(double[][] P, double[] L, double[] P_ex) {
		double[][][] ExL = new double[2][RHOSIZE][GRIDSIZE];
		for (int r=0; r<RHOSIZE; r++) {
			for (int k=0; k<GRIDSIZE; k++) {
				ExL[0][r][k] = L[k] + Math.log(P[r][k]) - Math.log(P_ex[r]);
				ExL[1][r][k] = L[k] + Math.log(1-P[r][k]) - Math.log(1-P_ex[r]);
			}
		}
		return ExL;
	}
	// expected entropy
	public static double[][] calExpectedEntropy(double[][][] L_ex) {
		double[][] ExH = new double[2][RHOSIZE];
		double H_win = 0;
		double H_lose = 0;
		for (int r=0; r<RHOSIZE; r++) {
			H_win = 0;
			H_lose = 0;
			for (int k=0; k<GRIDSIZE; k++) {
				H_win -= (L_ex[0][r][k] * Math.exp(L_ex[0][r][k]));
				H_lose -= (L_ex[1][r][k] * Math.exp(L_ex[1][r][k]));
			}
			ExH[0][r] = H_win;
			ExH[1][r] = H_lose;
		}
		return ExH;
	}
	// Expected Information gain
	public static double[] calEIG(double H, double[] P_ex, double[][] H_ex) {
		double[] ig = new double[RHOSIZE];
		for (int r=0; r<RHOSIZE; r++) {
			ig[r] = H - (P_ex[r]*H_ex[0][r]) - ((1-P_ex[r])*H_ex[1][r]);
		}
		return ig;
	}
	public static double calEntropy(double[] L) {
		// L is log likelihood of parameter (weight)
		double H = 0;
		for (int k=0; k<L.length; k++) {
			H -= (L[k] * Math.exp(L[k]));
		}
		return H;
	}

	
	// Utils
	public static int argmax(double[] arr) {
		int i_max = 0;
		for (int i=1; i<arr.length; i++) {
			if (arr[i_max] < arr[i]) {
				i_max = i;
			}
		}
		return i_max;
	}
	public static int argmin(double[] arr) {
		int i_min = 0;
		for (int i=1; i<arr.length; i++) {
			if (arr[i_min] > arr[i]) {
				i_min = i;
			}
		}
		return i_min;
	}

	
	// transform the L (array) to jsonarray
	public static JsonArray getLikelihood() {
		Gson gson = new Gson();
		JsonArray L = gson.toJsonTree(likelihood_bin).getAsJsonArray();
		return L;
	}
	public static JsonArray getBestParam() {
		int grid_best = argmax(likelihood_bin);
		int m_best = reshapeIndex(grid_best, 0);
		int w_best = reshapeIndex(grid_best, 1);
		int gamma_best = reshapeIndex(grid_best, 2);
		int lambda_best = reshapeIndex(grid_best, 3);

		int[] param_best = {m_best, w_best, gamma_best, lambda_best};
		Gson gson = new Gson();
		JsonArray theta = gson.toJsonTree(param_best).getAsJsonArray();
		return theta;
	}
	public static JsonArray getEIGs() {
		Gson gson = new Gson();
		JsonArray EIGs = gson.toJsonTree(EIG_bin).getAsJsonArray();
		return EIGs;
	}
	public static int getGridShape(int idx) {
		return CacModVariables.Psy_bin_param_shape.get(idx).getAsInt();
	}
	
}
