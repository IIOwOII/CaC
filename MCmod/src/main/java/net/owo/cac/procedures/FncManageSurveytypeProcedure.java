package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageSurveytypeProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Dat_survey_time = new com.google.gson.JsonArray();
		CacModVariables.Dat_survey_order = new com.google.gson.JsonArray();
		CacModVariables.Dat_survey_answer = new com.google.gson.JsonArray();
		if (CacModVariables.MapVariables.get(world).List_survey_preans.isEmpty()) {
			CacModVariables.MapVariables.get(world).List_survey_preans = CacModVariables.MapVariables.get(world).List_survey_initial.copy();
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		CacModVariables.Dat_survey_order.add(0);
		CacModVariables.Dat_survey_order.add(1);
		CacModVariables.Dat_survey_order.add(2);
		CacModVariables.Dat_survey_order.add(3);
		CacModVariables.Dat_survey_order.add(4);
	}
}
