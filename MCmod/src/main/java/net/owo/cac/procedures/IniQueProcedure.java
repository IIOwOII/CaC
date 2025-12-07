package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniQueProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_session = new com.google.gson.JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_que));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_session = obj_file.get(CacModVariables.Exp_session).getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.Ev_content = obj_session.get("initial").getAsString();
		CacModVariables.Ev_que = obj_session.get("trial").getAsJsonArray();
		CacModVariables.Ev_que_index = 0;
	}
}
