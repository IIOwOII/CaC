package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniPoolPsychometricProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_interval = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_step = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_m = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_w = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_gamma = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_lambda = new com.google.gson.JsonArray();
		double step_m = 0;
		double step_w = 0;
		double step_gamma = 0;
		double step_lambda = 0;
		double param_temp = 0;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_psychometric));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_interval = obj_file.get("interval").getAsJsonObject();
				obj_step = obj_file.get("step").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		step_m = obj_step.get("m").getAsDouble();
		step_w = obj_step.get("w").getAsDouble();
		step_gamma = obj_step.get("gamma").getAsDouble();
		step_lambda = obj_step.get("lambda").getAsDouble();
		arr_m = obj_interval.get("m").getAsJsonArray();
		arr_w = obj_interval.get("w").getAsJsonArray();
		arr_gamma = obj_interval.get("gamma").getAsJsonArray();
		arr_lambda = obj_interval.get("lambda").getAsJsonArray();
		CacModVariables.Psy_param_m = new ListTag();
		param_temp = arr_m.get(0).getAsDouble();
		while (param_temp <= arr_m.get(1).getAsDouble()) {
			CacModVariables.Psy_param_m.addTag(CacModVariables.Psy_param_m.size(), DoubleTag.valueOf(param_temp));
			param_temp = Math.round(Math.pow(10, 2) * (param_temp + step_m)) / Math.pow(10, 2);
		}
		CacModVariables.Psy_param_w = new ListTag();
		param_temp = arr_w.get(0).getAsDouble();
		while (param_temp <= arr_w.get(1).getAsDouble()) {
			CacModVariables.Psy_param_w.addTag(CacModVariables.Psy_param_w.size(), DoubleTag.valueOf(param_temp));
			param_temp = Math.round(Math.pow(10, 2) * (param_temp + step_w)) / Math.pow(10, 2);
		}
		CacModVariables.Psy_param_gamma = new ListTag();
		param_temp = arr_gamma.get(0).getAsDouble();
		while (param_temp <= arr_gamma.get(1).getAsDouble()) {
			CacModVariables.Psy_param_gamma.addTag(CacModVariables.Psy_param_gamma.size(), DoubleTag.valueOf(param_temp));
			param_temp = Math.round(Math.pow(10, 2) * (param_temp + step_gamma)) / Math.pow(10, 2);
		}
		CacModVariables.Psy_param_lambda = new ListTag();
		param_temp = arr_lambda.get(0).getAsDouble();
		while (param_temp <= arr_lambda.get(1).getAsDouble()) {
			CacModVariables.Psy_param_lambda.addTag(CacModVariables.Psy_param_lambda.size(), DoubleTag.valueOf(param_temp));
			param_temp = Math.round(Math.pow(10, 2) * (param_temp + step_lambda)) / Math.pow(10, 2);
		}
	}
}
