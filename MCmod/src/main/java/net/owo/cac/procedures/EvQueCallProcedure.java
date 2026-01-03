package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class EvQueCallProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonArray arr_content = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_absolute = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_relative = new com.google.gson.JsonArray();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_log = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_pool = new com.google.gson.JsonObject();
		double ev_duration = 0;
		if (!CacModVariables.Ev_occuring && !world.isClientSide()) {
			CacModVariables.Ev_occuring = true;
			if (CacModVariables.Log_type.contains("E")) {
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_event));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						obj_log = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						obj_cac = obj_log.get("cac").getAsJsonObject();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				arr_content = obj_cac.get("content").getAsJsonArray();
				arr_absolute = obj_cac.get("absolute").getAsJsonArray();
				arr_relative = obj_cac.get("relative").getAsJsonArray();
				arr_content.add(CacModVariables.Ev_content);
				arr_absolute.add(((int) CacModVariables.TimA_time));
				arr_relative.add(((int) CacModVariables.TimR_time));
				{
					com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
					try {
						FileWriter fileWriter = new FileWriter(CacModVariables.Log_event);
						fileWriter.write(mainGSONBuilderVariable.toJson(obj_log));
						fileWriter.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_event));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj_pool = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					ev_duration = obj_pool.get(CacModVariables.Ev_content).getAsDouble();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			CacModVariables.TimR_que_time = CacModVariables.TimR_time + ev_duration;
			EvInvokeProcedure.execute(world, entity);
		}
	}
}
