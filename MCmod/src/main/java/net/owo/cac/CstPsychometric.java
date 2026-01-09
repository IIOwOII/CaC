package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	
	public static double funcLogistic(double rho, double a, double b, double c, double d) {
		double y = c + (1-c-d)/(1+Math.exp((rho-a)/b));
		return y;
	}

	public static double funcPrior(double Da, double Db, double Dc, double Dd) {
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D = Math.pow(Da, 2) + Math.pow(Db, 2) + Math.pow(Dc, 2) + Math.pow(Dd, 2);
		double L = 0.5 + 0.5*Math.exp(-D/4);
		return L;
	}

	public static int flattenIndex(double sa, double sb, double sc, double sd) {
		int SA = (int)sa;
		int SB = (int)sb;
		int SC = (int)sc;
		int SD = (int)sd;
		int GA = (int)CacModVariables.Psy_quest_param_shape.get(0).getAsDouble();
		int GB = (int)CacModVariables.Psy_quest_param_shape.get(1).getAsDouble();
		int GC = (int)CacModVariables.Psy_quest_param_shape.get(2).getAsDouble();
		int GD = (int)CacModVariables.Psy_quest_param_shape.get(3).getAsDouble();
		
		int idx = SA*GB*GC*GD + SB*GC*GD + SC*GD + SD;
		
		return idx;
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
	
}
