package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class TaskPostRunProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_que = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_session = new com.google.gson.JsonObject();
		CacModVariables.Switch_blank = true;
		CacModVariables.Ev_que_loop = false;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_que));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_que = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_session = obj_que.get(CacModVariables.Exp_session).getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.Ev_content = obj_session.get("ending").getAsString();
	}
}
