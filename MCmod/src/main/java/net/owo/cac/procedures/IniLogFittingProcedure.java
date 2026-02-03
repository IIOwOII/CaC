package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniLogFittingProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_final = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_history = new com.google.gson.JsonObject();
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
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_fitting));
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
		obj_task.add("final", obj_final);
		obj_task.add("history", obj_history);
		obj_cac.add((CacModVariables.Psy_task + "_" + CacModVariables.Psy_method + "_" + CacModVariables.Psy_function), obj_task);
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
