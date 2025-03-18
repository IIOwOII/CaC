package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class TaskLogEventProcedure {
	public static void execute(LevelAccessor world) {
		File log_event = new File("");
		com.google.gson.JsonObject obj_event_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_event_sub = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_sub = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_empty = new com.google.gson.JsonArray();
		log_event = new File((CacModVariables.MapVariables.get(world).Exp_path + "/" + CacModVariables.MapVariables.get(world).Exp_session), File.separator + "log_event.json");
		try {
			log_event.getParentFile().mkdirs();
			log_event.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		obj_event_sub.add("content", arr_empty);
		obj_event_sub.add("absolute", arr_empty);
		obj_event_sub.add("relative", arr_empty);
		obj_event_main.add("cac", obj_event_sub);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(log_event);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_event_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
