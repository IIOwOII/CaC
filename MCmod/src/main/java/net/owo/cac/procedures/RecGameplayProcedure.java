package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecGameplayProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_pred = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_type = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_spawnpoint_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_winlose = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_time = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_difficulty = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_winrate = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_percentile = new com.google.gson.JsonArray();
		double dat_rho = 0;
		double dat_tick = 0;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_gameplay));
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
		arr_time = obj_cac.get("time").getAsJsonArray();
		arr_type = obj_cac.get("type").getAsJsonArray();
		arr_difficulty = obj_cac.get("difficulty").getAsJsonArray();
		arr_spawnpoint_opponent = obj_cac.get("spawnpoint_opponent").getAsJsonArray();
		arr_winlose = obj_cac.get("winlose").getAsJsonArray();
		arr_time.add(((int) CacModVariables.Dat_time_gameplay));
		arr_type.add(((int) CacModVariables.Dat_trial_type));
		arr_difficulty.add(CacModVariables.Dat_difficulty);
		arr_spawnpoint_opponent.add(((int) CacModVariables.Dat_trial_spawnpoint_opponent));
		arr_winlose.add(((int) CacModVariables.Dat_trial_winlose));
		if ((CacModVariables.Exp_session).equals("chasing") || (CacModVariables.Exp_session).equals("chased")) {
			dat_rho = CacModVariables.Dat_difficulty;
			dat_tick = CacModVariables.Dat_time_gameplay;
			obj_pred = obj_cac.get("predicted").getAsJsonObject();
			arr_winrate = obj_pred.get("winrate").getAsJsonArray();
			arr_percentile = obj_pred.get("percentile").getAsJsonArray();
			arr_winrate.add(net.owo.cac.CstPsychometric.calFitPSI(dat_rho));
			arr_percentile.add(net.owo.cac.CstPsychometric.calFitPercentile(dat_rho, dat_tick));
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_gameplay);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
