package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TestMixedStartProcedure {
	public static void execute(LevelAccessor world) {
		TaskPreRunProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Timer_show = "subtitle";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_event = "start_test";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_time = 5;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
