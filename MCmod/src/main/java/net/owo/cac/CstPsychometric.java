package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.owo.cac.network.CacModVariables;
import com.google.gson.JsonArray;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	public static double[] likelihood_bin;
	public static double entropy_bin = 0;
	public static double[][] probability_bin;

	// parameter list
	public static int GRIDSIZE = 0;
	public static double[] M;
	public static double[] W;
	public static double[] GAMMA;
	public static double[] LAMBDA;

	// grid of difficulty
	public static double[] RHO;


	// initialize
	public static void initBin(int func_type) {
		GRIDSIZE = getGridShape(0) * getGridShape(1) * getGridShape(2) * getGridShape(3);

		initBinParam();
		initBinRho();
		initBinL();
		initBinP(func_type);

		initPrior();
	}
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
		RHO = new double[20];
		double RHO_min = 0.90;
		double RHO_step = 0.01;
		for (int i=0; i<20; i++) {
			RHO[i] = Math.round((RHO_min + i*RHO_step)*1000) / 1000.0;
		}
	}
	// initialize binary likelihood
	public static void initBinL() {
		likelihood_bin = new double[GRIDSIZE];
	}

	// initialize grid probability
	public static void initBinP(int func_type) {
		double P = 0;
		probability_bin = new double[GRIDSIZE][RHO.length];
		
		for (int k=0; k<GRIDSIZE; k++) {
			for (int r=0; r<RHO.length; r++) {
				P = calPSI(func_type, RHO[r], reshapeIndex(k,0), reshapeIndex(k,1), reshapeIndex(k,2), reshapeIndex(k,3));
				probability_bin[k][r] = P;
			}
		}
	}

	// Setting the prior
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
	
	// Calculate the psychometric function (CDF)
	public static double calPSI(int func_type, double rho, double m, double w, double gamma, double lambda) {
		double F = 0;
		if (func_type == 0) { // Logistic
			F = funcLogistic(rho, m, w);
		}
		return gamma + (1-gamma-lambda)*F;
	}
	
	// Flattening Index
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

	public static int getGridShape(int idx) {
		return CacModVariables.Psy_bin_param_shape.get(idx).getAsInt();
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

	// F Candidate
	public static double funcLogistic(double rho, double m, double w) {
		double y = 1 / (1 + Math.pow(9, (rho-m)/w));
		return y;
	}

	public static double[] calLikelihood(double[] L, double[] P, boolean iswin) {
		// L is log likelihood of parameter
		// P is win probability given difficulty
		double[] L_next = new double[L.length];
		double p = 0;
		double Z_log = Math.log(calZ(L, P, iswin));
		
		for (int k=0; k<L.length; k++) {
			if (iswin) {
				p = P[k];
			} else {
				p = 1 - P[k];
			}
			L_next[k] = L[k] + Math.log(p) - Z_log;
		}

		return L_next;
	}

	public static double calEntropy(double[] L) {
		// L is log likelihood of parameter (weight)
		double H = 0;
		for (int k=0; k<L.length; k++) {
			H -= (L[k] * Math.exp(L[k]));
		}
		return H;
	}

	public static double[] calInfoGain(double H_curr, double[] H_win, double[] H_lose, double[] PI) {
		double[] EIG = new double[H_win.length];
		for (int x=0; x<H_win.length; x++){
			EIG[x] = H_curr - PI[x]*H_win[x] - (1-PI[x])*H_lose[x];
		}
		return EIG;
	}

	public static double calZ(double[] L, double[] P, boolean iswin) {
		// Z is sum of P(x)*P(theta)
		double Z = 0;
		double p = 0;
		for (int k=0; k<L.length; k++) {
			if (iswin) {
				p = P[k];
			} else {
				p = 1-P[k];
			}
			Z += (p * Math.exp(L[k]));
		}
		return Z;
	}
	
	public static double[] softmax(double[] X) {
		double[] Y = new double[X.length];
		double Y_sum = 0;
		for (int i=0; i<X.length; i++) {
			Y[i] = Math.exp(X[i]);
			Y_sum += Y[i];
		}
		for (int j=0; j<X.length; j++) {
			Y[j] /= Y_sum;
		}
		return Y;
	}

	public static double logsumexp(double[] X) {
		double Y_sum = 0;
		for (int i=0; i<X.length; i++) {
			Y_sum += Math.exp(X[i]);
		}
		return Math.log(Y_sum);
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
}
