package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.IntTag;

public class FncManageSpawnOpponentProcedure {
	public static double execute(LevelAccessor world) {
		double idx_spawnpoint = 0;
		idx_spawnpoint = 0;
		CacModVariables.MapVariables.get(world).Dat_trial_spawn_opponent.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) idx_spawnpoint));
		return idx_spawnpoint;
	}
}
