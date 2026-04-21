package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogGameplayProcedure {
	public static void execute() {
		com.google.gson.JsonArray arr_empty = new com.google.gson.JsonArray();
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_pred = new com.google.gson.JsonObject();
		CacModVariables.Log_gameplay = new File(CacModVariables.Dir_behaviors_session, File.separator + "log_gameplay.json");
		if (!CacModVariables.Log_gameplay.exists()) {
			try {
				CacModVariables.Log_gameplay.getParentFile().mkdirs();
				CacModVariables.Log_gameplay.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_cac.add("time", arr_empty);
			obj_cac.add("type", arr_empty);
			obj_cac.add("difficulty", arr_empty);
			obj_cac.add("spawnpoint_opponent", arr_empty);
			obj_cac.add("winlose", arr_empty);
			if ((CacModVariables.Exp_session).equals("chasing") || (CacModVariables.Exp_session).equals("chased")) {
				obj_pred.add("winrate", arr_empty);
				obj_pred.add("percentile", arr_empty);
				obj_cac.add("predicted", obj_pred);
			}
			obj_file.add("cac", obj_cac);
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_gameplay);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
