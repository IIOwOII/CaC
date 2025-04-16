package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecGameplayProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_type = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_difficulty_absolute = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_difficulty_relative = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_spawnpoint_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_winlose = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_time = new com.google.gson.JsonArray();
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
		arr_difficulty_absolute = obj_cac.get("difficulty_absolute").getAsJsonArray();
		arr_difficulty_relative = obj_cac.get("difficulty_relative").getAsJsonArray();
		arr_spawnpoint_opponent = obj_cac.get("spawnpoint_opponent").getAsJsonArray();
		arr_winlose = obj_cac.get("winlose").getAsJsonArray();
		arr_time.add(((int) CacModVariables.MapVariables.get(world).Dat_time_gameplay));
		arr_type.add(((int) CacModVariables.MapVariables.get(world).Dat_trial_type));
		arr_difficulty_absolute.add(CacModVariables.MapVariables.get(world).Dat_difficulty_absolute);
		arr_difficulty_relative.add(CacModVariables.MapVariables.get(world).Dat_difficulty_relative);
		arr_spawnpoint_opponent.add(((int) CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent));
		arr_winlose.add(((int) CacModVariables.MapVariables.get(world).Dat_trial_winlose));
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
