package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class PrdDespawnOpponentProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).UUID_opponent = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
