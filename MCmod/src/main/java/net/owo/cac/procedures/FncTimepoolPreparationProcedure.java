package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class FncTimepoolPreparationProcedure {
	public static ListTag execute(LevelAccessor world) {
		File file = new File("");
		com.google.gson.JsonArray time_variance = new com.google.gson.JsonArray();
		double time_base = 0;
		double idx = 0;
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		ListTag lst_timepool;
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/cacutil/components"), File.separator + "timepool.json");
		if (!file.exists()) {
			CacModVariables.MapVariables.get(world).Log_error = "nonexist_file";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		lst_timepool = new ListTag();
		idx = 0;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_main = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_sub = obj_main.get("preparation").getAsJsonObject();
				time_base = obj_sub.get("base").getAsDouble();
				time_variance = obj_sub.get("variance").getAsJsonArray();
				for (int index0 = 0; index0 < (int) obj_sub.size(); index0++) {
					lst_timepool.addTag((int) idx, DoubleTag.valueOf((time_base + time_variance.get(((int) idx)).getAsDouble())));
					idx = idx + 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lst_timepool;
	}
}
