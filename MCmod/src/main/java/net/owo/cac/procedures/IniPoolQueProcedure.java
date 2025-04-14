package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniPoolQueProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_que = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_session = new com.google.gson.JsonObject();
		if (!CacModVariables.Pool_que.exists()) {
			CacModVariables.Pool_que = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_que.json");
		}
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
				obj_session = obj_que.get(CacModVariables.MapVariables.get(world).Exp_session).getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.Ev_que = obj_session.get("trial").getAsJsonArray();
		CacModVariables.Ev_que_index = 0;
	}
}
