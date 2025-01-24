package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniReadConfigProcedure {
	public static void execute(LevelAccessor world) {
		File file_spawnpoint = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_place = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_point = new com.google.gson.JsonArray();
		double idx_point = 0;
		double idx_pos = 0;
		ListTag pos_point;
		file_spawnpoint = new File((FMLPaths.GAMEDIR.get().toString() + "/cacutil/components"), File.separator + "pool_spawnpoint.json");
		if (!file_spawnpoint.exists()) {
			CacModVariables.MapVariables.get(world).Log_error = "nonexist_file";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Pool_spawn = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file_spawnpoint));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_main = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_place = obj_main.get("default").getAsJsonObject();
				arr_opponent = obj_place.get("opponent").getAsJsonArray();
				idx_point = 0;
				for (int index0 = 0; index0 < (int) arr_opponent.size(); index0++) {
					arr_point = arr_opponent.get(((int) idx_point)).getAsJsonArray();
					pos_point = new ListTag();
					idx_pos = 0;
					for (int index1 = 0; index1 < (int) arr_point.size(); index1++) {
						pos_point.addTag((int) idx_pos, DoubleTag.valueOf(arr_point.get(((int) idx_pos)).getAsDouble()));
						idx_pos = idx_pos + 1;
					}
					CacModVariables.MapVariables.get(world).Pool_spawn.addTag((int) idx_point, (pos_point.copy()));
					idx_point = idx_point + 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7edir: " + FMLPaths.GAMEDIR.get().toString() + "/cacutil/components")), false);
			for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Pool_spawn) {
				pos_point = dataelementiterator instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
				idx_pos = 0;
				for (int index2 = 0; index2 < pos_point.size(); index2++) {
					if (!world.isClientSide() && world.getServer() != null)
						world.getServer().getPlayerList()
								.broadcastSystemMessage(Component.literal(
										(new java.text.DecimalFormat("#").format(idx_pos) + ": " + (new java.text.DecimalFormat("###.#").format((pos_point.get((int) idx_pos)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D)))),
										false);
					idx_pos = idx_pos + 1;
				}
			}
		}
	}
}
