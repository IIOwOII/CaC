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
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	// Type
	public static int task_type = -1;
	public static int method_type = -1;
	public static int func_type = -1;
	public static boolean method_bin = false;
	public static boolean method_con = false;

	// parameter list
	public static int GRIDSIZE = 180000;
	public static double[] M; // [grid]
	public static double[] W; // [grid]
	public static double[] GAMMA; //[grid]
	public static double[] LAMBDA; //[grid]

	public static int GRIDCON = 180000;
	public static double[] CON_K; // [grid]
	public static double[] CON_M; //[grid]
	public static double[] CON_H; // [grid]
	public static double[] CON_W; // [grid]

	// grid of difficulty
	public static int RHOSIZE = 60;
	public static double[] RHO; // [diff]
	public static double T = 30; // terminate time
	public static double[][] X_coef; // [diff][grid]

	// Data
	// Current
	public static double[][] probability_bin; // [diff][grid]
	public static double[] likelihood_bin; // [grid]
	public static double entropy_bin = Math.log(GRIDSIZE); // PSI
	
	public static double[][] probability_con; // [diff][grid]
	public static double[] likelihood_con; // [grid]
	public static double entropy_con = Math.log(GRIDCON); // PSI

	// Expected
	public static double[] expected_P_bin; // [diff]
	public static double[][][] expected_L_bin; // [win/lose][diff][grid]
	public static double[][] expected_H_bin; // [win/lose][diff]
	public static double[] EIG_bin; // [diff]
	
	public static double[] expected_P_con; // [diff]
	public static double[][][] expected_L_con; // [win/lose][diff][grid]
	public static double[][] expected_H_con; // [win/lose][diff]
	public static double[] EIG_con; // [diff]

	public static double rho_best_bin = 0;
	public static double rho_best_con = 0;
	public static double rho_best = 0;

	// Terminal Rule
	public static double IG_THRESHOLD = 0.05; // How much the maximum IG must be less than.
	public static int RAW_THRESHOLD = 5; // How many consecutive trials are required to satisfy the conditions.

	// Traces
	public static ArrayList<Double> trace_IG_bin = new ArrayList<>();
	public static ArrayList<Double> trace_IG_con = new ArrayList<>();
	public static ArrayList<Double> trace_maxEIG_bin = new ArrayList<>();
	public static ArrayList<Double> trace_maxEIG_con = new ArrayList<>();

	// Safety
	public static double MU_MAX = 100; // mu < 100*T
	public static double RHO_MAX = 1.30;
	public static double RHO_MIN = 0.70;
	public static double PMIN = 1.0E-12; // point 12
	public static double PMAX = 1.0 - 1.0E-12; // point 12
	public static double TRIAL_MAX = 25;

	// Fitted Data
	public static double[] THETA_STAR = new double[4]; // k, m, h, w
	public static ArrayList<Integer> trace_winlose = new ArrayList<>();
	public static ArrayList<Double> trace_rho = new ArrayList<>();
	public static ArrayList<Integer> trace_level = new ArrayList<>();

	// Using Fitted parameter
	// Theta Star Load
	public static void loadThetaStar() {
		JsonArray arr_theta = CacModVariables.Dat_theta.deepCopy();
		THETA_STAR = new double[4];
		for (int i=0; i<4; i++) {
			THETA_STAR[i] = arr_theta.get(i).getAsDouble();
		}
		// chasing? or chased?
		task_type = (int)CacModVariables.Dat_trial_type;
		trace_winlose = new ArrayList<>();
		trace_rho = new ArrayList<>();
		trace_level = new ArrayList<>();
	}
	public static double calFitPSI(double rho) {
		double P = 0;
		double rho_hat = 0;
		k = THETA_STAR[0];
		m = THETA_STAR[1];
		h = THETA_STAR[2];
		w = THETA_STAR[3];
		if (task_type == 0) {
			rho_hat = 1/rho;
		} else if (task_type == 1) {
			rho_hat = rho;
		}
		double X = 0;
		if (rho_hat >= (h-w)+w/(MU_MAX-m)) {
			X = k/(m+(1/(1+((rho_hat-h)/w))));
		} else {
			X = k/MU_MAX;
			rho_hat = (h-w) + w/(MU_MAX-m);
			if (task_type == 0) {
				rho = 1/rho_hat;
			} else if (task_type == 1) {
				rho = rho_hat;
			}
			CacMod.LOGGER.info("rho out of range! adjusted rho: " + new java.text.DecimalFormat("##.####").format(rho));
		}
		double series = 0;
		for (int i=0; i<(int)k; i++) {
			series += Math.pow(X, i)/factorial(i);
		}
		double P_hat = Math.exp(-X) * series;
		if (task_type == 0) {
			P = 1-P_hat;
		} else if (task_type == 1) {
			P = P_hat;
		}
		P = Math.round(P*10000.0) / 10000.0; // 4 decimals
		return P;
	}
	public static double calFitInversePSI(double P) {
		double P_hat = 0;
		double series = 0;
		k = THETA_STAR[0];
		m = THETA_STAR[1];
		h = THETA_STAR[2];
		w = THETA_STAR[3];
		int num_iter = 10;
		double X = k;
		if (task_type == 0) {
			P_hat = 1 - P;
		} else if (task_type == 1) {
			P_hat = P;
		}
		for (int j=0; j<num_iter; j++) {
			for (int i=0; i<(int)k; i++) {
				series += Math.pow(X, i)/factorial(i);
			}
			series -= Math.exp(X)*P_hat;
			X = X + (factorial(k-1)/Math.pow(X, k-1)) * series;
		}
		double rho_hat = h+w*((1/((k/X)-m))-1);
		double rho = 0;
		if (task_type == 0) {
			rho = 1/rho_hat;
		} else if (task_type == 1) {
			rho = rho_hat;
		}
		if (rho > RHO_MAX) {
			CacMod.LOGGER.info("Max rho!" + new java.text.DecimalFormat("##.####").format(rho));
			rho = RHO_MAX;
		} else if (rho < RHO_MIN) {
			CacMod.LOGGER.info("Min rho!" + new java.text.DecimalFormat("##.####").format(rho));
			rho = RHO_MIN;
		}
		rho = Math.round(rho*10000.0) / 10000.0; // 4 decimals
		return rho;
	}
	public static double calFitPercentile(double rho, double tick) {
		double P = 0;
		double rho_hat = 0;
		k = THETA_STAR[0];
		m = THETA_STAR[1];
		h = THETA_STAR[2];
		w = THETA_STAR[3];
		if (task_type == 0) {
			rho_hat = 1/rho;
		} else if (task_type == 1) {
			rho_hat = rho;
		}
		t = tick/20.0;
		x = (k*t/T)*(1.0/(m+(1.0/(1+((rho_hat-h)/w)))));
		double series = 0;
		for (int i=0; i<(int)k; i++) {
			series += Math.pow(x, i)/factorial(i);
		}
		P = 1 - series * Math.exp(-x);
		P = Math.round(P*10000.0) / 10000.0; // 4 decimals
		return P;
	}
	
	// Adjusting Difficulty
	public static void adjustRho() {
		/*
		0.75 3/4, 0.6 3/5, 0.5 3/6, 0.4 2/5, 0.25 1/4
		 */
		boolean raw_lose = false;
		int wl_prev = -1;
		int lv_prev = -1;
		int lv = -1;
		double P_target = -1;
		double rho_next = -1;
		int tnum = trace_winlose.size();
		
		if (tnum > 0) {
			wl_prev = trace_winlose.get(tnum-1).getAsInt();
			lv_prev = trace_level.get(tnum-1).getAsInt();
			if (wl_prev == 0) { // lose before
				if (tnum < 5 || lv_prev == 1) { // least level or under 5 trials
					lv = lv_prev;
				} else {
					raw_lose = true;
					for (int i=0; i<5; i++) {
						if (trace_winlose.get(tnum-i-1).getAsInt() == 1) {
							raw_lose = false;
						}
					}
					if (raw_lose) { // 5 raw lose
						lv = lv_prev - 1;
					} else {
						lv = lv_prev;
					}
				}
			} else if (wl_prev == 1) { // win before
				lv = lv_prev + 1;
			}
		} else if (tnum == 0) { // first trial
			lv = 1;
		}
		
		if (lv > 1) {
			P_target = Math.round(10000.0/lv) / 10000.0;
		} else if (lv == 1) {
			P_target = 0.75;
		}
		rho_next = calFitInversePSI(P_target);

		trace_level.add(lv);
		trace_rho.add(rho_next);
		CacModVariables.Dat_difficulty = rho_next;
	}

	// get result from CstAgent
	public static void msgResult(int wl) {
		int tnum = trace_level.size();
		if (tnum == 0) return; // if not chasing and chased
		trace_winlose.add(wl);
	}
	
	// usage
	// initialize
	public static void initPsy() {
		initRho();
		initTrace();
		if (method_type == 0) {
			method_bin = true;
			method_con = true;
		} else if (method_type == 1) {
			method_bin = true;
			method_con = false;
		} else if (method_type == 2) {
			method_bin = false;
			method_con = true;
		}
		if (method_bin) initBin();
		if (method_con) initCon();
	}
	public static void initBin() {
		GRIDSIZE = getBinShape(0) * getBinShape(1) * getBinShape(2) * getBinShape(3);
		initBinParam();
		initBinL();
		initBinP();
		initBinPrior();
	}
	public static void initCon() {
		GRIDCON = getConShape(0) * getConShape(1) * getConShape(2) * getConShape(3);
		initConParam();
		initConL();
		initConP(); // CDF
		initConXcoef(); // PDF variable x
		initConPrior();
	}
	
	// Repeat (Before trial)
	public static void updateTrialBefore() {
		if (method_bin) {
			expected_P_bin = calExpectedProbability(probability_bin, likelihood_bin);
			expected_L_bin = calExpectedLikelihood(probability_bin, likelihood_bin, expected_P_bin);
			expected_H_bin = calExpectedEntropy(expected_L_bin);
			EIG_bin = calEIG(entropy_bin, expected_P_bin, expected_H_bin);
			rho_best_bin = RHO[argmax(EIG_bin)];
			trace_maxEIG_bin.add(max(EIG_bin));
		}
		if (method_con) {
			expected_P_con = calExpectedConProbability(probability_con, likelihood_con);
			expected_L_con = calExpectedConLikelihood(probability_con, likelihood_con, expected_P_con);
			expected_H_con = calExpectedConEntropy(expected_L_con);
			EIG_con = calEIG(entropy_con, expected_P_con, expected_H_con);
			rho_best_con = RHO[argmax(EIG_con)];
			trace_maxEIG_con.add(max(EIG_con));
		}
		
		if (method_bin && !method_con) {
			rho_best = rho_best_bin;
		} else if (!method_bin && method_con) {
			rho_best = rho_best_con;
		} else if (method_bin && method_con) { //both
			rho_best = rho_best_con;
		}
		CacModVariables.Dat_difficulty = rho_best;
	}
	
	// Repeat (After trial)
	public static void updateTrialAfter() {
		int winlose = (int) CacModVariables.Dat_trial_winlose;
		int rho_curr = getRhoIndex(CacModVariables.Dat_difficulty);
		int wl = 0;
		if (winlose == 1) {
			wl = 0;
		} else if (winlose == 0) {
			wl = 1;
		}
		double H_past = 0;
		if (method_bin) {
			updateBinIG(entropy_bin, expected_H_bin[wl][rho_curr]);
			likelihood_bin = expected_L_bin[wl][rho_curr].clone();
			entropy_bin = expected_H_bin[wl][rho_curr];
		}
		if (method_con) {
			H_past = entropy_con;
			likelihood_con = updateConLikelihood(likelihood_con.clone());
			entropy_con = updateConEntropy(likelihood_con);
			updateConIG(H_past, entropy_con);
		}
		checkTerminate();
	}
	
	// Terminate
	public static void checkTerminate() {
		ArrayList<Double> EIG_check = new ArrayList<>();
		if (method_con) {
			EIG_check = trace_maxEIG_con;
		} else {
			EIG_check = trace_maxEIG_bin;
		}
		int size = EIG_check.size();
		if (size < 5) {
			CacModVariables.Exp_trial_total = TRIAL_MAX;
			return;
		}
		
		boolean isend = true;
		for (int i=size-RAW_THRESHOLD; i<size; i++) {
			if (EIG_check.get(i) >= IG_THRESHOLD) {
				isend = false;
			}
		}
		if (isend) {
			CacModVariables.Exp_trial_total = 1;
		} else {
			CacModVariables.Exp_trial_total = TRIAL_MAX;
		}
	}

	// init functions
	// diff
	public static void initRho() {
		// rho : [0.8, 1.2)_0.01
		RHO = new double[RHOSIZE];
		double RHO_step = 0.01;
		for (int r=0; r<RHOSIZE; r++) {
			RHO[r] = Math.round((RHO_MIN + r*RHO_step)*100.0) / 100.0;
		}
	}
	// trace
	public static void initTrace() {
		trace_IG_bin = new ArrayList<>();
		trace_IG_con = new ArrayList<>();
		trace_maxEIG_bin = new ArrayList<>();
		trace_maxEIG_con = new ArrayList<>();
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
				P[k] = Math.round((p_min + k*p_step)*100.0) / 100.0;
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
		
		for (int i=0; i<4; i++) {
			double P[] = new double[getConShape(i)];
			double p_min = param_min.get(i).getAsDouble();
			double p_max = param_max.get(i).getAsDouble();
			double p_step = param_step.get(i).getAsDouble();
			for (int k=0; k<P.length; k++) {
				P[k] = Math.round((p_min + k*p_step)*1000) / 1000.0;
			}
			if (i==0) {
				CON_K = P.clone();
			} else if (i==1) {
				CON_M = P.clone();
			} else if (i==2) {
				CON_H = P.clone();
			} else if (i==3) {
				CON_W = P.clone();
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
		int[] IDX = new int[4];
		for (int k=0; k<GRIDCON; k++) {
			IDX[0] = reshapeConIndex(k, 0);
			IDX[1] = reshapeConIndex(k, 1);
			IDX[2] = reshapeConIndex(k, 2);
			IDX[3] = reshapeConIndex(k, 3);
			for (int r=0; r<RHOSIZE; r++) {
				P = calPolyExpPSI(RHO[r], CON_K[IDX[0]], CON_M[IDX[1]], CON_H[IDX[2]], CON_W[IDX[3]]);
				probability_con[r][k] = P;
			}
		}
	}
	public static void initConXcoef() {
		X_coef = new double[RHOSIZE][GRIDCON];
		double coef = 0;
		double rho_hat = 0;
		int[] IDX = new int[4];
		for (int k=0; k<GRIDCON; k++) {
			IDX[0] = reshapeConIndex(k, 0);
			IDX[1] = reshapeConIndex(k, 1);
			IDX[2] = reshapeConIndex(k, 2);
			IDX[3] = reshapeConIndex(k, 3);
			for (int r=0; r<RHOSIZE; r++) {
				if (task_type == 0) {
					rho_hat = 1/RHO[r];
				} else if (task_type == 1) {
					rho_hat = RHO[r];
				}
				coef = calXcoef(rho_hat, CON_K[IDX[0]], CON_M[IDX[1]], CON_H[IDX[2]], CON_W[IDX[3]]);
				X_coef[r][k] = coef;
			}
		}
	}

	// Prior
	public static void initBinPrior() {
		// json get
		JsonArray param_prior = CacModVariables.Psy_bin_param_prior;
		double[] prior = new double[4];
		int[] shape = new int[4];
		for (int i=0; i<4; i++) {
			prior[i] = param_prior.get(i).getAsDouble();
			shape[i] = getBinShape(i);
		}
		// 1/2 * e^(-(a^2+b^2+...)^2/2) + 1/2
		double D_sq = 0;
		for (int a=0; a<shape[0]; a++) {
			for (int b=0; b<shape[1]; b++) {
				for (int c=0; c<shape[2]; c++) {
					for (int d=0; d<shape[3]; d++) {
						D_sq = Math.pow((a-prior[0])/shape[0],2) + Math.pow((b-prior[1])/shape[1],2) + Math.pow((c-prior[2])/shape[2],2) + Math.pow((d-prior[3])/shape[3],2);
						likelihood_bin[flattenIndex(a,b,c,d)] = Math.log(0.5+0.5*Math.exp(-D_sq/2));
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
		double[] prior = new double[4];
		int[] shape = new int[4];
		for (int i=0; i<4; i++) {
			prior[i] = param_prior.get(i).getAsDouble();
			shape[i] = getConShape(i);
		}
		// 1/2 * e^(-(a^2+b^2+...)^2/2) + 1/2
		double D_sq = 0;
		for (int a=0; a<shape[0]; a++) {
			for (int b=0; b<shape[1]; b++) {
				for (int c=0; c<shape[2]; c++) {
					for (int d=0; d<shape[3]; d++) {
						D_sq = Math.pow((a-prior[0])/shape[0],2) + Math.pow((b-prior[1])/shape[1],2) + Math.pow((c-prior[2])/shape[2],2) + Math.pow((d-prior[3])/shape[3],2);
						likelihood_con[flattenConIndex(a,b,c,d)] = Math.log(0.5+0.5*Math.exp(-D_sq/2));
					}
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
		JsonObject obj_trial = new JsonObject();
		JsonObject obj_method_bin = new JsonObject();
		JsonObject obj_method_con = new JsonObject();
		
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
		
		obj_trial.addProperty("difficulty", CacModVariables.Dat_difficulty);
		if (method_bin) {
			obj_method_bin.addProperty("rho_best", rho_best_bin);
			obj_method_bin.addProperty("entropy", entropy_bin);
			obj_method_bin.add("EIGs", getEIGs());
			obj_method_bin.add("param_best", getBestParam());
			//obj_method_bin.add("likelihood", getLikelihood());
			obj_trial.add(("binary"), obj_method_bin);
		}
		if (method_con) {
			obj_method_con.addProperty("rho_best", rho_best_con);
			obj_method_con.addProperty("entropy", entropy_con);
			obj_method_con.add("EIGs", getConEIGs());
			obj_method_con.add("param_best", getConBestParam());
			//obj_method_con.add("likelihood", getConLikelihood());
			obj_trial.add(("continuous"), obj_method_con);
		}
		
		obj_cac.add(("trial" + "_" + new java.text.DecimalFormat("##").format(CacModVariables.Exp_trial)), obj_trial);
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
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_final = new JsonObject();
		JsonObject obj_method_bin = new JsonObject();
		JsonObject obj_method_con = new JsonObject();
		
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
		
		obj_final = obj_cac.get("final").getAsJsonObject();

		if (method_bin) {
			obj_method_bin.addProperty("entropy", entropy_bin);
			obj_method_bin.add("param_best", getBestParam());
			obj_method_bin.add("likelihood", getLikelihood());
			obj_final.add(("binary"), obj_method_bin);
		}
		if (method_con) {
			obj_method_con.addProperty("entropy", entropy_con);
			obj_method_con.add("param_best", getConBestParam());
			obj_method_con.add("likelihood", getConLikelihood());
			obj_final.add(("continuous"), obj_method_con);
		}
		
		com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
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
		double PSI = 0;
		double F = 0;
		if (func_type == 0) { // Logistic
			F = funcLogistic(rho, m, w);
		}
		PSI = gamma + (1-gamma-lambda)*F;
		if (PSI < PMIN) {
			PSI = PMIN;
		} else if (PSI > PMAX) {
			PSI = PMAX;
		}
		return PSI;
	}
	public static double calPolyExpPSI(double rho, double k, double m, double h, double w) {
		// rho is original rho
		double PSI = 0;
		double rho_hat = 0;
		double P_hit = 0;
		double series = 0;
		double X = 0;
		if (task_type == 0) { // chasing
			rho_hat = 1/rho;
		} else if (task_type == 1) { // chased
			rho_hat = rho;
		}
		X = calX(T, rho_hat, k, m, h, w);
		for (int i=0; i<(int)k; i++) {
			series += (Math.pow(X,i)/factorial(i));
		}
		P_hit = 1 - Math.exp(-X) * series;
		if (task_type == 0) {
			PSI = P_hit;
		} else if (task_type == 1) {
			PSI = 1-P_hit;
		}
		if (PSI < PMIN) {
			PSI = PMIN;
		} else if (PSI > PMAX) {
			PSI = PMAX;
		}
		return PSI;
	}
	public static double calX(double t, double rho_hat, double k, double m, double h, double w) {
		// x = kt/mu
		double x = 0;
		x = (k*t)/calMu(rho_hat, k, m, h, w);
		return x;
	}
	public static double calXcoef(double rho_hat, double k, double m, double h, double w) {
		// x = kt/mu
		// calculate k/mu
		double x_coef = 0;
		x_coef = k/calMu(rho_hat, k, m, h, w);
		return x_coef;
	}
	public static double funcLogistic(double rho, double m, double w) {
		double y = 1 / (1 + Math.pow(9, (rho-m)/w));
		return y;
	}
	public static double calMu(double rho_hat, double k, double m, double h, double w) {
		double mu = 0;
		if (rho_hat >= (h-w)+(w/(MU_MAX-m))) {
			mu = T*(m+(1.0/(1+((rho_hat-h)/w))));
		} else {
			mu = MU_MAX*T;
		}
		return mu;
	}
	
	// Index rearrange
	public static int getRhoIndex(double rho) {
		int rho_id = -1;
		rho = Math.round(rho*100.0)/100.0;
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
	public static int flattenConIndex(int sk, int sm, int sh, int sw) {
		int GA = getConShape(0);
		int GB = getConShape(1);
		int GC = getConShape(2);
		int GD = getConShape(3);
		return sk*GB*GC*GD + sm*GC*GD + sh*GD + sw;
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
		int GD = getConShape(3);
		if (idx_param == 3) { // w
			idx = sgrid % GD;
		} else if (idx_param == 2) { // h
			idx = (sgrid / GD) % GC;
		} else if (idx_param == 1) { // m
			idx = ((sgrid / GD) / GC) % GB;
		} else if (idx_param == 0) { // k
			idx = ((sgrid / GD) / GC) / GB;
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


	// Update
	// Info gain
	public static void updateBinIG(double H, double H_next) {
		double IG = H - H_next;
		trace_IG_bin.add(IG);
	}
	public static void updateConIG(double H, double H_next) {
		double IG = H - H_next;
		trace_IG_con.add(IG);
	}

	// con L
	public static double[] updateConLikelihood(double[] L) {
		double[] L_update = new double[GRIDCON];
		int winlose = (int) CacModVariables.Dat_trial_winlose;
		int rho_curr = getRhoIndex(CacModVariables.Dat_difficulty);
		int wl = 0;
		if (winlose == 1) {
			wl = 0;
		} else if (winlose == 0) {
			wl = 1;
		}
		double t = (CacModVariables.Dat_time_gameplay/20.0); // sec
		double coef = 0;
		double x = 0;
		double k = 0;
		
		if ((task_type == 0 && winlose == 0) || (task_type == 1 && winlose == 1)) { // update based on P
			// over 600 (chasing lose) (chased win)
			L_update = expected_L_con[wl][rho_curr].clone();
		} else { // update based on t
			for (int g=0; g<GRIDCON; g++) {
				coef = X_coef[rho_curr][g];
				x = coef * t;
				k = CON_K[reshapeConIndex(g, 0)];
				
				L_update[g] = L[g] + k*Math.log(x) - x - Math.log(t) - Math.log((double)(factorial(k-1)));
			}
			L_update = normL(L_update.clone());
		}
		return L_update;
	}

	// con H
	public static double updateConEntropy(double[] L) {
		double H_update = 0;
		int winlose = (int) CacModVariables.Dat_trial_winlose;
		int rho_curr = getRhoIndex(CacModVariables.Dat_difficulty);
		int wl = 0;
		if (winlose == 1) {
			wl = 0;
		} else if (winlose == 0) {
			wl = 1;
		}

		// update based on P
		// over 600 (chasing lose) (chased win)
		if ((task_type == 0 && winlose == 0) || (task_type == 1 && winlose == 1)) { 
			H_update = expected_H_con[wl][rho_curr];
		} else {
			// update based on t
			// warning : please update L before
			H_update = calEntropy(L);
		}
		return H_update;
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
	public static double[] calExpectedConProbability(double[][] P, double[] L) {
		double[] ExP = new double[RHOSIZE];
		double ExP_temp = 0;
		for (int r=0; r<RHOSIZE; r++) {
			ExP_temp = 0;
			for (int k=0; k<GRIDCON; k++) {
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
	public static double[][][] calExpectedConLikelihood(double[][] P, double[] L, double[] P_ex) {
		double[][][] ExL = new double[2][RHOSIZE][GRIDCON];
		double sp = 0;
		double sp_ex = 0;
		for (int r=0; r<RHOSIZE; r++) {
			sp_ex = P_ex[r];
			if (sp_ex < PMIN) {
				sp_ex = PMIN;
			} else if (sp_ex > PMAX) {
				sp_ex = PMAX;
			}
			for (int k=0; k<GRIDCON; k++) {
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
	public static double[][] calExpectedConEntropy(double[][][] L_ex) {
		double[][] ExH = new double[2][RHOSIZE];
		double H_win = 0;
		double H_lose = 0;
		for (int r=0; r<RHOSIZE; r++) {
			H_win = 0;
			H_lose = 0;
			for (int k=0; k<GRIDCON; k++) {
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

	// Current Entropy
	public static double calEntropy(double[] L) {
		// L is log likelihood of parameter (weight)
		double H = 0;
		for (int k=0; k<L.length; k++) {
			H -= (L[k] * Math.exp(L[k]));
		}
		return H;
	}
	
	
	// Utils
	public static double max(double[] arr) {
		double v_max = arr[0];
		for (int i=1; i<arr.length; i++) {
			if (v_max < arr[i]) {
				v_max = arr[i];
			}
		}
		return v_max;
	}
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
	public static long factorial(int n) {
		// 20! < 2^63
		long value = 1L;
		if (n==0) return value; // 0!=1
		for (int k=1; k<n+1; k++) {
			value *= k;
		}
		return value;
	}
	public static long factorial(double m) {
		// 20! < 2^63
		int n = (int)m;
		long value = 1L;
		if (n==0) return value; // 0!=1
		for (int k=1; k<n+1; k++) {
			value *= k;
		}
		return value;
	}

	// get rho given estimated win rate P_target.
	public static int getEstimatedRho(double P_target){
		int grid_best = argmax(likelihood_bin);
		int rho_est_idx = 0;
		double prob_diff = Math.abs(probability_bin[0][grid_best] - P_target);
		double prob_diff_temp = 0;
		for (int r=1; r<RHOSIZE; r++) {
			prob_diff_temp = Math.abs(probability_bin[r][grid_best] - P_target);
			if (prob_diff > prob_diff_temp) {
				rho_est_idx = r;
				prob_diff = prob_diff_temp;
			}
		}
		return rho_est_idx;
	}
	public static int getConEstimatedRho(double P_target){
		int grid_best = argmax(likelihood_con);
		int rho_est_idx = 0;
		double prob_diff = Math.abs(probability_con[0][grid_best] - P_target);
		double prob_diff_temp = 0;
		for (int r=1; r<RHOSIZE; r++) {
			prob_diff_temp = Math.abs(probability_con[r][grid_best] - P_target);
			if (prob_diff > prob_diff_temp) {
				rho_est_idx = r;
				prob_diff = prob_diff_temp;
			}
		}
		return rho_est_idx;
	}
	
	// transform the L (array) to jsonarray
	// get L
	public static JsonArray getLikelihood() {
		Gson gson = new Gson();
		JsonArray L = gson.toJsonTree(likelihood_bin).getAsJsonArray();
		return L;
	}
	public static JsonArray getConLikelihood() {
		Gson gson = new Gson();
		JsonArray L = gson.toJsonTree(likelihood_con).getAsJsonArray();
		return L;
	}

	// get theta
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
	public static JsonArray getConBestParam() {
		int grid_best = argmax(likelihood_con);
		int k_best = reshapeConIndex(grid_best, 0);
		int m_best = reshapeConIndex(grid_best, 1);
		int h_best = reshapeConIndex(grid_best, 2);
		int w_best = reshapeConIndex(grid_best, 3);
		int[] param_best = {k_best, m_best, h_best, w_best};
		
		Gson gson = new Gson();
		JsonArray theta = gson.toJsonTree(param_best).getAsJsonArray();
		return theta;
	}

	// get EIGs
	public static JsonArray getEIGs() {
		Gson gson = new Gson();
		JsonArray EIGs = gson.toJsonTree(EIG_bin).getAsJsonArray();
		return EIGs;
	}
	public static JsonArray getConEIGs() {
		Gson gson = new Gson();
		JsonArray EIGs = gson.toJsonTree(EIG_con).getAsJsonArray();
		return EIGs;
	}

	// get Shape
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
