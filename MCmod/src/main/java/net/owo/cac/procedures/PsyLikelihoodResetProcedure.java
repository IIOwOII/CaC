package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

public class PsyLikelihoodResetProcedure {
	public static void execute(LevelAccessor world) {
		double num_grid = 0;
		double num_index = 0;
		CacModVariables.MapVariables.get(world).Psy_likelihood = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		num_grid = CacModVariables.MapVariables.get(world).Psy_param_m.size() * CacModVariables.MapVariables.get(world).Psy_param_w.size() * CacModVariables.MapVariables.get(world).Psy_param_gamma.size()
				* CacModVariables.MapVariables.get(world).Psy_param_lambda.size();
		num_index = 0;
		while (num_index < num_grid) {
			CacModVariables.MapVariables.get(world).Psy_likelihood.addTag((int) num_index, DoubleTag.valueOf(0));
			num_index = Math.round(num_index + 1);
		}
	}
}
