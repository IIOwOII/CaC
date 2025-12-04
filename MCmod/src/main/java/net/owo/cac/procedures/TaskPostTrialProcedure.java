package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskPostTrialProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Exp_phase = 5;
		CacModVariables.Exp_trial = CacModVariables.Exp_trial + 1;
		CacModVariables.MapVariables.get(world).Switch_blank = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.Exp_trial >= CacModVariables.Exp_trial_total) {
			TaskPostRunProcedure.execute(world);
		}
	}
}
