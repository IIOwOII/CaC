package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniLogPositionTrialProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonArray arr_empty = new com.google.gson.JsonArray();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_role = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
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
		obj_role.add("x", arr_empty);
		obj_role.add("z", arr_empty);
		obj_role.add("r", arr_empty);
		obj_trial.add("player", obj_role);
		obj_trial.add("opponent", obj_role);
		obj_cac.add(("trial_" + new java.text.DecimalFormat("##").format(CacModVariables.MapVariables.get(world).Exp_trial)), obj_trial);
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
