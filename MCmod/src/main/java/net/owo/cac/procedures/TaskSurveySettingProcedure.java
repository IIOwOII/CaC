package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurveySettingProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 3;
		CacModVariables.MapVariables.get(world).syncData(world);
		FncManageSurveytypeProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Dat_survey_index = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
