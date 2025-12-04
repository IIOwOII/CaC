package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogSurveyProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_reference = new com.google.gson.JsonArray();
		CacModVariables.Log_survey = new File(CacModVariables.Dir_behaviors_session, File.separator + "log_survey.json");
		if (!CacModVariables.Log_survey.exists()) {
			try {
				CacModVariables.Log_survey.getParentFile().mkdirs();
				CacModVariables.Log_survey.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			arr_reference = new com.google.gson.JsonArray();
			arr_reference.add("winprob");
			arr_reference.add("perdiff");
			arr_reference.add("stress");
			arr_reference.add("target");
			arr_reference.add("control");
			obj_cac.add("reference", arr_reference);
			obj_file.add("cac", obj_cac);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_survey);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
