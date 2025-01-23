package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSessionEndProcedure {
	public static void execute(LevelAccessor world) {
		RecSessionResultProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Switch_blank = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_exp = false;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
