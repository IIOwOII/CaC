package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskPostTrialProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 4;
		CacModVariables.MapVariables.get(world).syncData(world);
		RecTrialresultProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Exp_trial = CacModVariables.MapVariables.get(world).Exp_trial + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Exp_trial >= CacModVariables.MapVariables.get(world).Exp_trial_total) {
			TaskPostRunProcedure.execute(world);
		}
	}
}
