package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class TutoMissionUpdateProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_tutorial = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_mission = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_beginner = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_checkpoint = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_racing = new com.google.gson.JsonArray();
		double state_beginner = 0;
		double state_checkpoint = 0;
		double state_racing = 0;
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
				obj_tutorial = obj_file.get("tutorial").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_mission = obj_tutorial.get("mission").getAsJsonObject();
		arr_beginner = obj_mission.get("beginner").getAsJsonArray();
		arr_checkpoint = obj_mission.get("checkpoint").getAsJsonArray();
		arr_racing = obj_mission.get("racing").getAsJsonArray();
		if (entity instanceof ServerPlayer _plr6 && _plr6.level() instanceof ServerLevel && _plr6.getAdvancements().getOrStartProgress(_plr6.server.getAdvancements().getAdvancement(new ResourceLocation("cac:adv_racing"))).isDone()) {
			state_racing = 14;
			state_checkpoint = 10;
			state_beginner = 6;
		} else if (entity instanceof ServerPlayer _plr7 && _plr7.level() instanceof ServerLevel && _plr7.getAdvancements().getOrStartProgress(_plr7.server.getAdvancements().getAdvancement(new ResourceLocation("cac:adv_checkpoint"))).isDone()) {
			state_racing = 13;
			state_checkpoint = 10;
			state_beginner = 6;
		} else if (entity instanceof ServerPlayer _plr8 && _plr8.level() instanceof ServerLevel && _plr8.getAdvancements().getOrStartProgress(_plr8.server.getAdvancements().getAdvancement(new ResourceLocation("cac:adv_beginner"))).isDone()) {
			state_racing = 12;
			state_checkpoint = 9;
			state_beginner = 6;
		} else {
			state_racing = 12;
			state_checkpoint = 8;
			state_beginner = 5;
		}
		{
			int _value = (int) state_beginner;
			BlockPos _pos = BlockPos.containing(arr_beginner.get(0).getAsDouble(), arr_beginner.get(1).getAsDouble(), arr_beginner.get(2).getAsDouble());
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
		{
			int _value = (int) state_checkpoint;
			BlockPos _pos = BlockPos.containing(arr_checkpoint.get(0).getAsDouble(), arr_checkpoint.get(1).getAsDouble(), arr_checkpoint.get(2).getAsDouble());
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
		{
			int _value = (int) state_racing;
			BlockPos _pos = BlockPos.containing(arr_racing.get(0).getAsDouble(), arr_racing.get(1).getAsDouble(), arr_racing.get(2).getAsDouble());
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
	}
}
