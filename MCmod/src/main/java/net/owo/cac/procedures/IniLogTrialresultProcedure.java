package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogTrialresultProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonArray arr_empty = new com.google.gson.JsonArray();
		com.google.gson.JsonObject obj_event_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_event_sub = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_sub = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		CacModVariables.Log_trialresult = new File((CacModVariables.MapVariables.get(world).Exp_path + "/" + CacModVariables.MapVariables.get(world).Exp_session), File.separator + "log_trialresult.json");
		try {
			CacModVariables.Log_trialresult.getParentFile().mkdirs();
			CacModVariables.Log_trialresult.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		obj_cac.add("type", arr_empty);
		obj_cac.add("difficulty_absolute", arr_empty);
		obj_cac.add("difficulty_relative", arr_empty);
		obj_cac.add("spawnpoint_opponent", arr_empty);
		obj_cac.add("winlose", arr_empty);
		obj_cac.add("time", arr_empty);
		obj_file.add("cac", obj_cac);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_trialresult);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
