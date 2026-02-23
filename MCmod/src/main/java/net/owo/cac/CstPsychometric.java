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
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.apache.commons.io.IOIndexedException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	// Type
	public static int task_type = -1;
	public static int method_type = -1;
	public static int func_type = -1;
	
	// Current
	public static double[][] probability_bin; // [diff][grid]
	public static double[] likelihood_bin; // [grid]
	public static double entropy_bin = 0;
	
	public static double[][] probability_con; // [diff][grid]
	public static double[] likelihood_con; // [grid]
	public static double entropy_con = 0; // PSI
	

	// Expected
	public static double[] expected_P_bin; // [diff]
	public static double[][][] expected_L_bin; // [win/lose][diff][grid]
	public static double[][] expected_H_bin; // [win/lose][diff]
	public static double[] EIG_bin; // [diff]
	
	public static double[] expected_P_con; // [diff]
	public static double[][][] expected_L_con; // [win/lose][diff][grid]
	public static double[][] expected_H_con; // [win/lose][diff]
	public static double[] EIG_con; // [diff]
	
	public static double rho_best = 0;

	// parameter list
	public static int GRIDSIZE = 10000;
	public static double[] M; // [grid]
	public static double[] W; // [grid]
	public static double[] GAMMA; //[grid]
	public static double[] LAMBDA; //[grid]

	public static int GRIDCON = 800;
	public static double[] K; // [grid]
	public static double[] A; // [grid]
	public static double[] B; // [grid]

	// grid of difficulty
	public static int RHOSIZE = 20;
	public static double[] RHO; // [diff]
	public static double T = 30; // terminate time

	// Terminal Rule
	public static double[] IG_last = new double[3];
	public static double IG_THRESHOLD = 0.1;

	// Safety
	public static double PMIN = 1.0E-12; // point 12
	public static double PMAX = 1.0 - 1.0E-12; // point 12
	

	// usage
	// initialize
	public static void initPsy() {
		initRho();
		if (method_type == 0) {
			initBin();
		}
	}
	public static void initBin() {
		GRIDSIZE = getBinShape(0) * getBinShape(1) * getBinShape(2) * getBinShape(3);
		initBinParam();
		initBinL();
		initBinP();
		initBinPrior();
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
		boolean isend = true;
		for (int i=0; i<IG_last.length; i++) {
			if ((IG_last[i] >= IG_THRESHOLD) || (IG_last[i] == 0)) {
				isend = false;
			}
		}
		if (isend) {
			CacModVariables.Exp_trial_total = 1;
		} else {
			CacModVariables.Exp_trial_total = 20;
		}
	}


	// init functions
	// diff
	public static void initRho() {
		// rho : [0.9, 1.1)_0.01
		RHO = new double[RHOSIZE];
		double RHO_min = 0.90;
		double RHO_step = 0.01;
		for (int r=0; r<RHOSIZE; r++) {
			RHO[r] = Math.round((RHO_min + r*RHO_step)*1000) / 1000.0;
		}
	}

	// param
	public static void initBinParam() {
		JsonArray param_min = CacModVariables.Psy_bin_param_min;
		JsonArray param_max = CacModVariables.Psy_bin_param_max;
		JsonArray param_step = CacModVariables.Psy_bin_param_step;
		
		for (int i=0; i<4; i++) {
			double P[] = new double[getBinShape(i)];
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
	public static void initConParam() {
		JsonArray param_min = CacModVariables.Psy_con_param_min;
		JsonArray param_max = CacModVariables.Psy_con_param_max;
		JsonArray param_step = CacModVariables.Psy_con_param_step;
		
		for (int i=0; i<3; i++) {
			double P[] = new double[getConShape(i)];
			double p_min = param_min.get(i).getAsDouble();
			double p_max = param_max.get(i).getAsDouble();
			double p_step = param_step.get(i).getAsDouble();
			for (int k=0; k<P.length; k++) {
				P[k] = Math.round((p_min + k*p_step)*1000) / 1000.0;
			}
			if (i==0) {
				K = P.clone();
			} else if (i==1) {
				A = P.clone();
			} else if (i==2) {
				B = P.clone();
			}
		}
	}

	// likelihood
	public static void initBinL() {
		likelihood_bin = new double[GRIDSIZE];
	}
	public static void initConL() {
		likelihood_con = new double[GRIDCON];
	}

	// Prob
	public static void initBinP() {
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
	public static void initConP() {
		probability_con = new double[RHOSIZE][GRIDCON];
		double P = 0;
		int[] IDX = new int[3];
		for (int k=0; k<GRIDCON; k++) {
			IDX[0] = reshapeConIndex(k, 0);
			IDX[1] = reshapeConIndex(k, 1);
			IDX[2] = reshapeConIndex(k, 2);
			for (int r=0; r<RHOSIZE; r++) {
				P = calPolyExpPSI(RHO[r], K[IDX[0]], A[IDX[1]], B[IDX[2]]);
				probability_con[r][k] = P;
			}
		}
	}

	// Prior
	public static void initBinPrior() {
		// json get
		JsonArray param_prior = CacModVariables.Psy_bin_param_prior;
		int pm = param_prior.get(0).getAsInt();
		int pw = param_prior.get(1).getAsInt();
		int pgamma = param_prior.get(2).getAsInt();
		int plambda = param_prior.get(3).getAsInt();
		
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D_sq = 0;
		for (int sm=0; sm<getBinShape(0); sm++) {
			for (int sw=0; sw<getBinShape(1); sw++) {
				for (int sgamma=0; sgamma<getBinShape(2); sgamma++) {
					for (int slambda=0; slambda<getBinShape(3); slambda++) {
						D_sq = Math.pow(sm-pm, 2) + Math.pow(sw-pw, 2) + Math.pow(sgamma-pgamma, 2) + Math.pow(slambda-plambda, 2);
						likelihood_bin[flattenIndex(sm, sw, sgamma, slambda)] = Math.log(0.5 + 0.5*Math.exp(-D_sq/4));
					}
				}
			}
		}
		likelihood_bin = normL(likelihood_bin.clone());
		entropy_bin = calEntropy(likelihood_bin);
	}
	public static void initConPrior() {
		// json get
		JsonArray param_prior = CacModVariables.Psy_con_param_prior;
		int pk = param_prior.get(0).getAsInt();
		int pa = param_prior.get(1).getAsInt();
		int pb = param_prior.get(2).getAsInt();
		
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D_sq = 0;
		for (int sk=0; sk<getConShape(0); sk++) {
			for (int sa=0; sa<getConShape(1); sa++) {
				for (int sb=0; sb<getConShape(2); sb++) {
					D_sq = Math.pow(sk-pk, 2) + Math.pow(sa-pa, 2) + Math.pow(sb-pb, 2);
					likelihood_con[flattenConIndex(sk, sa, sb)] = Math.log(0.5 + 0.5*Math.exp(-D_sq/4));
				}
			}
		}
		likelihood_con = normL(likelihood_con.clone());
		entropy_con = calEntropy(likelihood_con);
	}


	// Recording Data on log file
	public static void recHistory() {
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_task = new JsonObject();
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
		obj_trial.addProperty("difficulty", CacModVariables.Dat_difficulty);
		obj_trial.addProperty("entropy", entropy_bin);
		obj_trial.add("EIGs", getEIGs());
		obj_trial.add("param_best", getBestParam());
		obj_trial.add("likelihood", getLikelihood());
		obj_task.add(("trial" + "_" + new java.text.DecimalFormat("##").format(CacModVariables.Exp_trial)), obj_trial);
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
	public static double calPolyExpPSI(double rho, double k, double a, double b) {
		// rho is original rho
		double rho_hat = 0;
		double P_hit = 0;
		double series = 0;
		double X = 0;
		if (task_type == 0) { // chasing
			rho_hat = 1/rho;
		} else if (task_type == 1) { // chased
			rho_hat = rho;
		}
		X = calX(T, rho_hat, k, a, b);
		for (int i=0; i<(int)k; i++) {
			series += (Math.pow(X,i)/factorial(i));
		}
		P_hit = 1 - Math.exp(-X) * series;
		if (task_type == 0) {
			return P_hit;
		} else if (task_type == 1) {
			return 1-P_hit;
		}
	}
	public static double calX(double t, double rho_hat, double k, double a, double b) {
		// x = kt/mu
		double x = 0;
		x = k*t*((b/10)*rho_hat - (a/15));
		return x;
	}
	public static double funcLogistic(double rho, double m, double w) {
		double y = 1 / (1 + Math.pow(9, (rho-m)/w));
		return y;
	}
	
	
	// Index rearrange
	public static int getRhoIndex(double rho) {
		int rho_id = -1;
		for (int r=0; r<RHOSIZE; r++) {
			if (RHO[r] == rho) {
				rho_id = r;
			}
		}
		return rho_id;
	}
	
	public static int flattenIndex(int sm, int sw, int sgamma, int slambda) {
		int GA = getBinShape(0);
		int GB = getBinShape(1);
		int GC = getBinShape(2);
		int GD = getBinShape(3);
		return sm*GB*GC*GD + sw*GC*GD + sgamma*GD + slambda;
	}
	public static int flattenConIndex(int sk, int sa, int sb) {
		int GA = getConShape(0);
		int GB = getConShape(1);
		int GC = getConShape(2);
		return sk*GB*GC + sa*GC + sb;
	}
	
	public static int reshapeIndex(int sgrid, int idx_param) {
		int idx = 0;
		int GA = getBinShape(0);
		int GB = getBinShape(1);
		int GC = getBinShape(2);
		int GD = getBinShape(3);
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
	public static int reshapeConIndex(int sgrid, int idx_param) {
		int idx = 0;
		int GA = getConShape(0);
		int GB = getConShape(1);
		int GC = getConShape(2);
		if (idx_param == 2) { // b
			idx = sgrid % GC;
		} else if (idx_param == 1) { // a
			idx = (sgrid / GC) % GB;
		} else if (idx_param == 0) { // k
			idx = (sgrid / GC) / GB;
		}
		return idx;
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
		double sp = 0;
		double sp_ex = 0;
		for (int r=0; r<RHOSIZE; r++) {
			sp_ex = P_ex[r];
			if (sp_ex < PMIN) {
				sp_ex = PMIN;
			} else if (sp_ex > PMAX) {
				sp_ex = PMAX;
			}
			for (int k=0; k<GRIDSIZE; k++) {
				sp = P[r][k];
				if (sp < PMIN) {
					sp = PMIN;
				} else if (sp > PMAX) {
					sp = PMAX;
				}
				ExL[0][r][k] = L[k] + Math.log(sp) - Math.log(sp_ex);
				ExL[1][r][k] = L[k] + Math.log(1-sp) - Math.log(1-sp_ex);
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
	public static int factorial(int n) {
		int value = 1;
		if (n==0) return value;
		for (int k=1; k<n+1; k++) {
			value *= k;
		}
		return value;
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
	
	public static int getBinShape(int idx) {
		return CacModVariables.Psy_bin_param_shape.get(idx).getAsInt();
	}
	public static int getConShape(int idx) {
		return CacModVariables.Psy_con_param_shape.get(idx).getAsInt();
	}


	// Debug
	public static void debugValue() {
		// curr
		for (int r=0; r<RHOSIZE; r++) {
			for (int k=0; k<GRIDSIZE; k++) {
				if ((Double.isNaN(probability_bin[r][k])) || (Double.isInfinite(probability_bin[r][k]))) {
					System.out.printf("bug: P %n");
					System.out.printf("rho: %d %n", r);
					System.out.printf("grid: %d %n", k);
					return;
				}
			}
		}
		for (int k=0; k<GRIDSIZE; k++) {
			if ((Double.isNaN(likelihood_bin[k])) || (Double.isInfinite(likelihood_bin[k]))) {
				System.out.printf("bug: L %n");
				System.out.printf("grid: %d %n", k);
				return;
			}
		}
		if ((Double.isNaN(entropy_bin)) || (Double.isInfinite(entropy_bin))) {
			System.out.printf("bug: H %n");
			return;
		}
		// ex
		for (int r=0; r<RHOSIZE; r++) {
			if ((Double.isNaN(expected_P_bin[r])) || (Double.isInfinite(expected_P_bin[r]))) {
				System.out.printf("bug: exP %n");
				System.out.printf("rho: %d %n", r);
				return;
			}
		}
		for (int r=0; r<RHOSIZE; r++) {
			for (int k=0; k<GRIDSIZE; k++) {
				if (((Double.isNaN(expected_L_bin[0][r][k])) || (Double.isNaN(expected_L_bin[1][r][k]))) || ((Double.isInfinite(expected_L_bin[0][r][k])) || (Double.isInfinite(expected_L_bin[1][r][k])))) {
					System.out.printf("bug: exL %n");
					System.out.printf("rho: %d %n", r);
					System.out.printf("grid: %d %n", k);
					return;
				}
			}
		}
		for (int r=0; r<RHOSIZE; r++) {
			if (((Double.isNaN(expected_H_bin[0][r])) || (Double.isNaN(expected_H_bin[1][r]))) || ((Double.isInfinite(expected_H_bin[0][r])) || (Double.isInfinite(expected_H_bin[1][r])))) {
				System.out.printf("bug: exH %n");
				System.out.printf("rho: %d %n", r);
				return;
			}
		}
		for (int r=0; r<RHOSIZE; r++) {
			if ((Double.isNaN(EIG_bin[r])) || (Double.isInfinite(EIG_bin[r]))) {
				System.out.printf("bug: EIG %n");
				System.out.printf("rho: %d %n", r);
				return;
			}
		}
	}
}
