package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class EvPulseRecordProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_content = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_absolute = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_relative = new com.google.gson.JsonArray();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_event));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_main = obj_file.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		arr_content = obj_main.get("content").getAsJsonArray();
		arr_content.add(CacModVariables.MapVariables.get(world).Ev_pulse_content);
		arr_absolute = obj_main.get("absolute").getAsJsonArray();
		arr_absolute.add(CacModVariables.MapVariables.get(world).TimA_time_currtick);
		arr_relative = obj_main.get("relative").getAsJsonArray();
		arr_relative.add(CacModVariables.MapVariables.get(world).TimR_time);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_event);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		CacModVariables.MapVariables.get(world).Ev_pulse_content = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
