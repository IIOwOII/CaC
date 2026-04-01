package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecPositionProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_gameplay = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_preparation = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_prey = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_predator = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_prey_prep = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_predator_prep = new com.google.gson.JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_position));
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
		if (!CacModVariables.Dat_pos_time_prep.isEmpty()) {
			obj_prey_prep.add("x", CacModVariables.Dat_prey_x_prep);
			obj_prey_prep.add("z", CacModVariables.Dat_prey_z_prep);
			obj_prey_prep.add("r", CacModVariables.Dat_prey_r_prep);
			obj_predator_prep.add("x", CacModVariables.Dat_predator_x_prep);
			obj_predator_prep.add("z", CacModVariables.Dat_predator_z_prep);
			obj_predator_prep.add("r", CacModVariables.Dat_predator_r_prep);
			obj_preparation.add("time", CacModVariables.Dat_pos_time_prep);
			obj_preparation.add("prey", obj_prey_prep);
			obj_preparation.add("predator", obj_predator_prep);
			obj_trial.add("preparation", obj_preparation);
		}
		if (!CacModVariables.Dat_pos_time.isEmpty()) {
			obj_prey.add("x", CacModVariables.Dat_prey_x);
			obj_prey.add("z", CacModVariables.Dat_prey_z);
			obj_prey.add("r", CacModVariables.Dat_prey_r);
			obj_predator.add("x", CacModVariables.Dat_predator_x);
			obj_predator.add("z", CacModVariables.Dat_predator_z);
			obj_predator.add("r", CacModVariables.Dat_predator_r);
			obj_gameplay.add("time", CacModVariables.Dat_pos_time);
			obj_gameplay.add("prey", obj_prey);
			obj_gameplay.add("predator", obj_predator);
			obj_trial.add("gameplay", obj_gameplay);
		}
		obj_cac.add(("trial_" + (int) CacModVariables.Exp_trial), obj_trial);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_position);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
