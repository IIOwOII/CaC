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
		if (CacModVariables.Exp_trial >= CacModVariables.Exp_trial_total) {
			TaskPostRunProcedure.execute();
		}
	}
}
