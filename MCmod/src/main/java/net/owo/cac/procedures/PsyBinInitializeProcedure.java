package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class PsyBinInitializeProcedure {
	public static void execute() {
		double num_gridsize = 0;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_quest = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_param = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_bin = new com.google.gson.JsonObject();
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
				obj_bin = obj_file.get("binary").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_param = obj_quest.get("parameter").getAsJsonObject();
		CacModVariables.Psy_bin_param_min = obj_param.get("min").getAsJsonArray();
		CacModVariables.Psy_bin_param_max = obj_param.get("max").getAsJsonArray();
		CacModVariables.Psy_bin_param_step = obj_param.get("step").getAsJsonArray();
		CacModVariables.Psy_bin_param_shape = obj_param.get("shape").getAsJsonArray();
		CacModVariables.Psy_bin_param_prior = obj_param.get("prior").getAsJsonArray();
		num_gridsize = CacModVariables.Psy_bin_param_shape.get(0).getAsDouble() * CacModVariables.Psy_bin_param_shape.get(1).getAsDouble() * CacModVariables.Psy_bin_param_shape.get(2).getAsDouble()
				* CacModVariables.Psy_bin_param_shape.get(3).getAsDouble();
		CacModVariables.Psy_bin_L = new ListTag();
		CacModVariables.Psy_bin_P = new ListTag();
		for (int index0 = 0; index0 < (int) num_gridsize; index0++) {
			CacModVariables.Psy_bin_L.addTag(0, DoubleTag.valueOf(0));
			CacModVariables.Psy_bin_P.addTag(0, DoubleTag.valueOf(0));
		}
	}
}
