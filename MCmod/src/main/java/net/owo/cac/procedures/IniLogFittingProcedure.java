package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogFittingProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		CacModVariables.Log_fitting = new File(CacModVariables.Dir_behaviors_session, File.separator + "log_fitting.json");
		if (!CacModVariables.Log_fitting.exists()) {
			try {
				CacModVariables.Log_fitting.getParentFile().mkdirs();
				CacModVariables.Log_fitting.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_file.add("cac", obj_cac);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_fitting);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
