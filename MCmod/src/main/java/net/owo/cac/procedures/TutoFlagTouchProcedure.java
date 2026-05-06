package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class TutoFlagTouchProcedure {
	public static void execute(LevelAccessor world) {
		double prac_id = 0;
		com.google.gson.JsonArray arr_route = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_pos = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_end = new com.google.gson.JsonArray();
		com.google.gson.JsonObject obj_point = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_prac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_tuto = new com.google.gson.JsonObject();
		net.owo.cac.CstTutorial.prac_id++;
		prac_id = net.owo.cac.CstTutorial.prac_id;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_point));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_point = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_tuto = obj_point.get("tutorial").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_prac = obj_tuto.get("practice").getAsJsonObject();
		arr_route = obj_prac.get("route").getAsJsonArray();
		if (prac_id < arr_route.size()) {
			arr_pos = arr_route.get(((int) prac_id)).getAsJsonArray();
			{
				int _value = 1;
				BlockPos _pos = BlockPos.containing(arr_pos.get(0).getAsDouble(), arr_pos.get(1).getAsDouble(), arr_pos.get(2).getAsDouble());
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
					world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
			}
		} else {
			net.owo.cac.CstTutorial.prac_id = -1;
		}
	}
}
