package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecSurveyProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_time = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_order = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_answer = new com.google.gson.JsonArray();
		if (CacModVariables.MapVariables.get(world).Log_type.contains("S")) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_survey));
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
			for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_survey_order) {
				arr_order.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
			}
			for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_survey_time) {
				arr_time.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
			}
			for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_survey_answer) {
				arr_answer.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
			}
			obj_trial.add("order", arr_order);
			obj_trial.add("time", arr_time);
			obj_trial.add("answer", arr_answer);
			obj_trial.addProperty("surrender", ((int) CacModVariables.MapVariables.get(world).Dat_survey_surrender));
			obj_trial.addProperty("surrender type", ((int) CacModVariables.MapVariables.get(world).Dat_survey_surrender_type));
			obj_cac.add(("trial_" + (int) CacModVariables.MapVariables.get(world).Exp_trial), obj_trial);
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
