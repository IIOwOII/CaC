package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

public class PrdExperimentalProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_pool = new com.google.gson.JsonObject();
		CacModVariables.Dat_survey_order = new com.google.gson.JsonArray();
		CacModVariables.Dat_survey_order.add(0);
		CacModVariables.Dat_survey_order.add(1);
		CacMod.LOGGER.info(CacModVariables.Dat_survey_order);
	}
}
