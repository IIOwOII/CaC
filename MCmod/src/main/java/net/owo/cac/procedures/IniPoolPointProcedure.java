package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniPoolPointProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonArray arr_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_point = new com.google.gson.JsonArray();
		double idx_point = 0;
		double idx_pos = 0;
		ListTag pos_point;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_spawnpoint = new com.google.gson.JsonObject();
		CacModVariables.Pool_point = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_point.json");
		CacModVariables.MapVariables.get(world).List_spawnpoint_opponent = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_point));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_spawnpoint = obj_file.get("spawnpoint").getAsJsonObject();
				arr_opponent = obj_spawnpoint.get("opponent").getAsJsonArray();
				idx_point = 0;
				for (int index0 = 0; index0 < (int) arr_opponent.size(); index0++) {
					arr_point = arr_opponent.get(((int) idx_point)).getAsJsonArray();
					pos_point = new ListTag();
					idx_pos = 0;
					for (int index1 = 0; index1 < (int) arr_point.size(); index1++) {
						pos_point.addTag((int) idx_pos, DoubleTag.valueOf(arr_point.get(((int) idx_pos)).getAsDouble()));
						idx_pos = idx_pos + 1;
					}
					CacModVariables.MapVariables.get(world).List_spawnpoint_opponent.addTag((int) idx_point, (pos_point.copy()));
					idx_point = idx_point + 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).List_spawnpoint_opponent) {
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
