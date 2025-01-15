package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class PrdDespawnPlayerProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).UUID_player = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
