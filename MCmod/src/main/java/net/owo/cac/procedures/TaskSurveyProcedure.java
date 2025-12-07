package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurveyProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Exp_phase = 3;
		if (world.isClientSide()) {
			net.owo.cac.CstSurvey.startSurvey();
		}
	}
}
