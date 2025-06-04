package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurveyProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 3;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Exp_trial == 0) {
			CacModVariables.MapVariables.get(world).SuvT_answer_pre = CacModVariables.MapVariables.get(world).Suv_initial.copy();
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		CacModVariables.MapVariables.get(world).SuvT_index = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		FncManageSurveyResetProcedure.execute(world);
		FncManageSurveyOrderProcedure.execute(world);
	}
}
