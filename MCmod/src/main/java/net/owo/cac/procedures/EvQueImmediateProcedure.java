package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class EvQueImmediateProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).TimR_que_end = CacModVariables.MapVariables.get(world).TimR_time + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
