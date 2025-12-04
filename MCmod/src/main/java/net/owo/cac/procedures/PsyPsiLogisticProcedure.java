package net.owo.cac.procedures;

import org.checkerframework.checker.units.qual.m;

import net.owo.cac.network.CacModVariables;

public class PsyPsiLogisticProcedure {
	public static double execute() {
		double rho = 0;
		double m = 0;
		double w = 0;
		double gamma = 0;
		double lambda = 0;
		double p = 0;
		rho = CacModVariables.Dat_difficulty_absolute;
		p = gamma + ((1 - lambda) - gamma) / (1 + Math.pow(2.71828183, 5.8889 * ((rho - m) / w)));
		return p;
	}
}
