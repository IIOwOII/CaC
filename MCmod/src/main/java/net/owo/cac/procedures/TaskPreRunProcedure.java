package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class TaskPreRunProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		TimAbsoluteSwitchProcedure.execute(world);
		TimRelativeSwitchProcedure.execute(world);
		IniLogPositionProcedure.execute(world);
		IniLogTrialresultProcedure.execute(world);
		IniLogEventProcedure.execute(world);
		IniPoolEventProcedure.execute();
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
		CacModVariables.MapVariables.get(world).Ev_content_next = "session_start";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvQueCallProcedure.execute(world, entity);
	}
}
