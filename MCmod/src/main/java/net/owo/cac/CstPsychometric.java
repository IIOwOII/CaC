package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPsychometric {
	
	public static double funcLogistic(double x, double a, double b, double c, double d) {
		double y = c + (1-c-d)/(1+Math.exp((x-a)/b));
		return y;
	}

	public static double funcPrior(double Da, double Db, double Dc, double Dd) {
		// 1/2 * e^(-(a^2+b^2+...)^2/4) + 1/2
		double D = Math.pow(Da, 2) + Math.pow(Db, 2) + Math.pow(Dc, 2) + Math.pow(Dd, 2);
		double L = 0.5 + 0.5*Math.exp(-D/4);
		return L;
	}
	
}
