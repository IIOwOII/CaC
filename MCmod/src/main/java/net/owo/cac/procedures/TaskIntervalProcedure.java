package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

public class TaskIntervalProcedure {
	public static void execute(LevelAccessor world) {
		double time_interval = 0;
		time_interval = FncManageIntervalProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Exp_trial = CacModVariables.MapVariables.get(world).Exp_trial + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_phase = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			if (CacModVariables.MapVariables.get(world).Exp_trial < CacModVariables.MapVariables.get(world).Dat_trial_total) {
				CacModVariables.MapVariables.get(world).Timer_event = "phase_preparation";
				CacModVariables.MapVariables.get(world).syncData(world);
			} else {
				CacModVariables.MapVariables.get(world).Timer_event = "end_session";
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			CacModVariables.MapVariables.get(world).Timer_time = time_interval;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
	}
}
