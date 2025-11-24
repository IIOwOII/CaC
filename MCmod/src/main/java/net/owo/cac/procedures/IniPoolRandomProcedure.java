package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniPoolRandomProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_preparation = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_spawn = new com.google.gson.JsonArray();
		double arr_size = 0;
		double idx = 0;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_random));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				arr_preparation = obj_file.get("preparation").getAsJsonArray();
				arr_spawn = obj_file.get("spawn").getAsJsonArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (arr_preparation.size() != arr_spawn.size()) {
			CacErrorProcedure.execute(world);
		} else {
			arr_size = arr_preparation.size();
		}
		idx = 0;
		CacModVariables.MapVariables.get(world).List_random_preparation = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_random_spawn = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		for (int index0 = 0; index0 < (int) arr_size; index0++) {
			CacModVariables.MapVariables.get(world).List_random_preparation.addTag((int) idx, DoubleTag.valueOf(arr_preparation.get(((int) idx)).getAsDouble()));
			CacModVariables.MapVariables.get(world).List_random_spawn.addTag((int) idx, DoubleTag.valueOf(arr_spawn.get(((int) idx)).getAsDouble()));
			idx = idx + 1;
		}
	}
}
