package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniPoolPointProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_spawnpoint = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_border = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_tutorial = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_checkpoint = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_point = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_offset = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_border_start = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_border_end = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_checkpoint_start = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_checkpoint_end = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_checkpoint_center = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_checkpoint_route = new com.google.gson.JsonArray();
		Vec3 vec_checkpoint_start = Vec3.ZERO;
		Vec3 vec_checkpoint_end = Vec3.ZERO;
		double idx_point = 0;
		double idx_pos = 0;
		double idx_route = 0;
		double sx = 0;
		double sz = 0;
		double sy = 0;
		double num_checkpoint_interval = 0;
		ListTag pos_point;
		ListTag pos_checkpoint;
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
				arr_offset = obj_file.get("offset").getAsJsonArray();
				obj_border = obj_file.get("border").getAsJsonObject();
				obj_spawnpoint = obj_file.get("spawnpoint").getAsJsonObject();
				obj_tutorial = obj_file.get("tutorial").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.MapVariables.get(world).Pos_offset = new Vec3(arr_offset.get(0).getAsDouble(), arr_offset.get(1).getAsDouble(), arr_offset.get(2).getAsDouble());
		CacModVariables.MapVariables.get(world).syncData(world);
		arr_border_start = obj_border.get("start").getAsJsonArray();
		arr_border_end = obj_border.get("end").getAsJsonArray();
		CacModVariables.MapVariables.get(world).Pos_border_start = new Vec3(arr_border_start.get(0).getAsDouble(), arr_border_start.get(1).getAsDouble(), arr_border_start.get(2).getAsDouble());
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Pos_border_end = new Vec3(arr_border_end.get(0).getAsDouble(), arr_border_end.get(1).getAsDouble(), arr_border_end.get(2).getAsDouble());
		CacModVariables.MapVariables.get(world).syncData(world);
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
		obj_checkpoint = obj_tutorial.get("checkpoint").getAsJsonObject();
		arr_checkpoint_start = obj_checkpoint.get("start").getAsJsonArray();
		arr_checkpoint_end = obj_checkpoint.get("end").getAsJsonArray();
		arr_checkpoint_center = obj_checkpoint.get("center").getAsJsonArray();
		arr_checkpoint_route = obj_checkpoint.get("route").getAsJsonArray();
		CacModVariables.Tuto_checkpoint_center = new Vec3(arr_checkpoint_center.get(0).getAsDouble(), arr_checkpoint_center.get(1).getAsDouble(), arr_checkpoint_center.get(2).getAsDouble());
		num_checkpoint_interval = obj_checkpoint.get("interval").getAsDouble();
		CacModVariables.Tuto_checkpoint_route = new ListTag();
		idx_route = 0;
		for (int index2 = 0; index2 < (int) arr_checkpoint_route.size(); index2++) {
			CacModVariables.Tuto_checkpoint_route.addTag((int) idx_route, IntTag.valueOf((int) arr_checkpoint_route.get(((int) idx_route)).getAsDouble()));
			idx_route = idx_route + 1;
		}
		vec_checkpoint_start = new Vec3(arr_checkpoint_start.get(0).getAsDouble(), arr_checkpoint_start.get(1).getAsDouble(), arr_checkpoint_start.get(2).getAsDouble());
		vec_checkpoint_end = new Vec3(arr_checkpoint_end.get(0).getAsDouble(), arr_checkpoint_end.get(1).getAsDouble(), arr_checkpoint_end.get(2).getAsDouble());
		CacModVariables.Tuto_checkpoint_pos = new ListTag();
		sy = CacModVariables.Tuto_checkpoint_center.y();
		sx = vec_checkpoint_start.x();
		sz = vec_checkpoint_start.z();
		while (sz <= vec_checkpoint_end.z()) {
			while (sx <= vec_checkpoint_end.x()) {
				pos_checkpoint = new ListTag();
				pos_checkpoint.addTag(0, IntTag.valueOf((int) sx));
				pos_checkpoint.addTag(1, IntTag.valueOf((int) sy));
				pos_checkpoint.addTag(2, IntTag.valueOf((int) sz));
				CacModVariables.Tuto_checkpoint_pos.addTag(CacModVariables.Tuto_checkpoint_pos.size(), (pos_checkpoint.copy()));
				sx = sx + num_checkpoint_interval;
			}
			sz = sz + num_checkpoint_interval;
			sx = vec_checkpoint_start.x();
		}
	}
}
