package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.IntTag;

public class FncManageTasktypeProcedure {
	public static double execute(LevelAccessor world) {
		double idx_type = 0;
		if (CacModVariables.MapVariables.get(world).Exp_trial % 2 == 0) {
			idx_type = 0;
		} else if (CacModVariables.MapVariables.get(world).Exp_trial % 2 == 1) {
			idx_type = 1;
		}
		CacModVariables.MapVariables.get(world).Dat_type_trial.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) idx_type));
		return idx_type;
	}
}
