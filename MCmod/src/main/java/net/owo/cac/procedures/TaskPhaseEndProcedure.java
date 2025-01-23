package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskPhaseEndProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Exp_phase == 2) {
			RecTrialResultProcedure.execute(world);
			CacModVariables.MapVariables.get(world).Timer_event = "phase_survey";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Timer_time = 5;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Exp_phase == 3) {
			CacModVariables.MapVariables.get(world).Timer_event = "phase_interval";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Timer_time = 5;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
