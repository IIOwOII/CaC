package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageSpawnOpponentProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = (CacModVariables.MapVariables.get(world).List_random_spawn.get((int) CacModVariables.MapVariables.get(world).Exp_trial)) instanceof DoubleTag _doubleTag
				? _doubleTag.getAsDouble()
				: 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
