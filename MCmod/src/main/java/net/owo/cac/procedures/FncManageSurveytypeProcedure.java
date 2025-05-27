package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageSurveytypeProcedure {
	public static void execute(LevelAccessor world) {
		double idx_survey = 0;
		CacModVariables.Dat_survey_time = new com.google.gson.JsonArray();
		CacModVariables.Dat_survey_order = new com.google.gson.JsonArray();
		CacModVariables.Dat_survey_answer = new com.google.gson.JsonArray();
		if (CacModVariables.MapVariables.get(world).Exp_trial == 0) {
			CacModVariables.MapVariables.get(world).List_survey_preans = CacModVariables.MapVariables.get(world).List_survey_initial.copy();
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		idx_survey = 0;
		for (int index0 = 0; index0 < CacModVariables.MapVariables.get(world).List_survey_name.size(); index0++) {
			CacModVariables.Dat_survey_order.add(((int) idx_survey));
			idx_survey = idx_survey + 1;
		}
	}
}
