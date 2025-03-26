package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskPostRunProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Ev_pulse_content = CacModVariables.MapVariables.get(world).Exp_session + "_end";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvPulseRecordProcedure.execute(world);
		TimAbsoluteSwitchProcedure.execute(world);
		TimRelativeSwitchProcedure.execute(world);
	}
}
