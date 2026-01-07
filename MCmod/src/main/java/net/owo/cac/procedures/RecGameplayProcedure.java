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
		com.google.gson.JsonArray arr_type = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_spawnpoint_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_winlose = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_time = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_difficulty = new com.google.gson.JsonArray();
		if (CacModVariables.Log_type.contains("G")) {
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
			arr_difficulty.add(CacModVariables.Dat_difficulty_absolute);
			arr_spawnpoint_opponent.add(((int) CacModVariables.Dat_trial_spawnpoint_opponent));
			arr_winlose.add(((int) CacModVariables.Dat_trial_winlose));
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
}
