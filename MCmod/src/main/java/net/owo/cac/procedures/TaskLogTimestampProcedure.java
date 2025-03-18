package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class TaskLogTimestampProcedure {
	public static void execute(LevelAccessor world) {
		File log_timestamp = new File("");
		com.google.gson.JsonObject obj_timestamp_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_sub = new com.google.gson.JsonObject();
		log_timestamp = new File(CacModVariables.MapVariables.get(world).Exp_path, File.separator + "log_timestamp.json");
		try {
			log_timestamp.getParentFile().mkdirs();
			log_timestamp.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		obj_timestamp_sub.addProperty("register", Calendar.getInstance().getTime().toString());
		obj_timestamp_main.add("cac", obj_timestamp_sub);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(log_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_timestamp_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
