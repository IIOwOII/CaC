package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageSpawnOpponentProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
