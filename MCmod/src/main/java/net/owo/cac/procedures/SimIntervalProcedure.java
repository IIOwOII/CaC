package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.Entity;

public class SimIntervalProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		EffRemoveMorphProcedure.execute(entity);
		RecPositionProcedure.execute();
		RecGameplayProcedure.execute();
		CacModVariables.Exp_trial = CacModVariables.Exp_trial + 1;
		CacModVariables.Dat_difficulty_absolute = Math.round(Math.pow(10, 2) * (0.8 + Math.floor(CacModVariables.Exp_trial / 10) * 0.01)) / Math.pow(10, 2);
		if (CacModVariables.Exp_trial >= CacModVariables.Exp_trial_total) {
			TaskPostRunProcedure.execute();
		}
	}
}
