package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	// Calculate the psychometric function (CDF)
	public static double calPSI(int func_type, double rho, double m, double w, double gamma, double lambda) {
		double F = 0;
		if (func_type == 0) { // Logistic
			F = funcLogistic(rho, m, w);
		}
		return gamma + (1-gamma-lambda)*F;
	}

	// Setting the prior
	public static double setPrior(double Dm, double Dw, double Dgamma, double Dlambda) {
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D = Math.pow(Dm, 2) + Math.pow(Dw, 2) + Math.pow(Dgamma, 2) + Math.pow(Dlambda, 2);
		double L = 0.5 + 0.5*Math.exp(-D/4);
		return L;
	}

	// Flattening Index
	public static int flattenIndex(double sm, double sw, double sgamma, double slambda) {
		int SA = (int)sm;
		int SB = (int)sw;
		int SC = (int)sgamma;
		int SD = (int)slambda;
		int GA = (int)CacModVariables.Psy_bin_param_shape.get(0).getAsDouble();
		int GB = (int)CacModVariables.Psy_bin_param_shape.get(1).getAsDouble();
		int GC = (int)CacModVariables.Psy_bin_param_shape.get(2).getAsDouble();
		int GD = (int)CacModVariables.Psy_bin_param_shape.get(3).getAsDouble();
		
		int idx = SA*GB*GC*GD + SB*GC*GD + SC*GD + SD;
		
		return idx;
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
