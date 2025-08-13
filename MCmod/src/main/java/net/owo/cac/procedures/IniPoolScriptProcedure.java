package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniPoolScriptProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		CacModVariables.Pool_script = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_script.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_script));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				CacModVariables.Scr_beginner = obj_file.get("beginner").getAsJsonArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
