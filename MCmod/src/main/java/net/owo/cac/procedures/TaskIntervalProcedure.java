package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskIntervalProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 4;
		CacModVariables.MapVariables.get(world).syncData(world);
		FncManageIntervalProcedure.execute(world);
		RecTrialresultProcedure.execute(world);
		RecPositionProcedure.execute(world);
	}
}
