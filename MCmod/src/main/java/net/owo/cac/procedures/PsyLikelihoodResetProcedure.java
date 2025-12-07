package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

public class PsyLikelihoodResetProcedure {
	public static void execute() {
		double num_grid = 0;
		double num_index = 0;
		CacModVariables.Psy_likelihood = new ListTag();
		num_grid = CacModVariables.Psy_param_m.size() * CacModVariables.Psy_param_w.size() * CacModVariables.Psy_param_gamma.size() * CacModVariables.Psy_param_lambda.size();
		num_index = 0;
		while (num_index < num_grid) {
			CacModVariables.Psy_likelihood.addTag((int) num_index, DoubleTag.valueOf(0));
			num_index = Math.round(num_index + 1);
		}
	}
}
