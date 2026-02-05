package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecFittingProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_history = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
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
		obj_task = obj_cac.get((CacModVariables.Psy_task + "_" + CacModVariables.Psy_method + "_" + CacModVariables.Psy_function)).getAsJsonObject();
		obj_history = obj_task.get("history").getAsJsonObject();
		obj_trial.addProperty("difficulty", CacModVariables.Dat_difficulty);
		obj_trial.addProperty("entropy", net.owo.cac.CstPsychometric.entropy_bin);
		obj_trial.add("param_best", net.owo.cac.CstPsychometric.getBestParam());
		obj_trial.add("likelihood", net.owo.cac.CstPsychometric.getLikelihood());
		obj_history.add(("trial_" + new java.text.DecimalFormat("##").format(CacModVariables.Exp_trial)), obj_trial);
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
