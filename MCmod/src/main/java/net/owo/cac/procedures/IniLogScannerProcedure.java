package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogScannerProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_empty = new com.google.gson.JsonArray();
		CacModVariables.Log_scanner = new File(CacModVariables.MapVariables.get(world).Dir_behaviors_session, File.separator + "log_scanner.json");
		if (!CacModVariables.Log_scanner.exists()) {
			try {
				CacModVariables.Log_scanner.getParentFile().mkdirs();
				CacModVariables.Log_scanner.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_cac.add("TR", arr_empty);
			obj_cac.add("absolute", arr_empty);
			obj_file.add("cac", obj_cac);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_scanner);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
