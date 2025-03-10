package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

import java.util.Calendar;

import java.io.File;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		File file = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		CacModVariables.MapVariables.get(world).Tim_experiment_switch = !CacModVariables.MapVariables.get(world).Tim_experiment_switch;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Tim_experiment_time = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Tim_experiment_tick = -1;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Tim_experiment_time_oldtick = Calendar.getInstance().getTimeInMillis();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacMod.LOGGER.info(Calendar.getInstance().getTime().toString());
	}
}
