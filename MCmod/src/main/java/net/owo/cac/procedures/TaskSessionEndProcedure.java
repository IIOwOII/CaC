package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class TaskSessionEndProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_timestamp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_timestamp));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_timestamp = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_cac = obj_timestamp.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_cac.addProperty((CacModVariables.MapVariables.get(world).Exp_session + "_end"), Calendar.getInstance().getTime().toString());
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_timestamp));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		CacModVariables.MapVariables.get(world).Switch_blank = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_timer = false;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
