package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurveyStartProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 2.9;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		FncManageSurveytypeProcedure.execute(world);
	}
}
