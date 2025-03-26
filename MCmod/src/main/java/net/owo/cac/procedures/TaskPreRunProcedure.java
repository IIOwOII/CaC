package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskPreRunProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_trial = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_phase = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_type = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_relative = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_winlose = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		TaskLogEventProcedure.execute(world);
		TimAbsoluteSwitchProcedure.execute(world);
		TimRelativeSwitchProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Ev_pulse_content = CacModVariables.MapVariables.get(world).Exp_session + "_start";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvPulseRecordProcedure.execute(world);
	}
}
