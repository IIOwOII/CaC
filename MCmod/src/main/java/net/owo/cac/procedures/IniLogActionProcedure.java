package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogActionProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		CacModVariables.Log_action = new File(CacModVariables.Dir_behaviors_session, File.separator + "log_action.json");
		if (!CacModVariables.Log_action.exists()) {
			try {
				CacModVariables.Log_action.getParentFile().mkdirs();
				CacModVariables.Log_action.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_file.add("cac", obj_cac);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_action);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
