package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecSurveyProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_time = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_order = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_answer = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_surrender = new com.google.gson.JsonArray();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_survey));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_cac = obj_file.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
