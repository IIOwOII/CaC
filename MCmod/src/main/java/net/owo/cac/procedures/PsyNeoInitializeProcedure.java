package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class PsyNeoInitializeProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_param = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_neo = new com.google.gson.JsonObject();
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
				obj_neo = obj_file.get("neo").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_param = obj_neo.get("parameter").getAsJsonObject();
		CacModVariables.Psy_neo_param_min = obj_param.get("min").getAsJsonArray();
		CacModVariables.Psy_neo_param_max = obj_param.get("max").getAsJsonArray();
		CacModVariables.Psy_neo_param_step = obj_param.get("step").getAsJsonArray();
		CacModVariables.Psy_neo_param_shape = obj_param.get("shape").getAsJsonArray();
		CacModVariables.Psy_neo_param_prior = obj_param.get("prior").getAsJsonArray();
	}
}
