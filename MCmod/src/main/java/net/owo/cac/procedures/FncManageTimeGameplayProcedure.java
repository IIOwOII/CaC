package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;

public class FncManageTimeGameplayProcedure {
	public static void execute(LevelAccessor world) {
		RandomSource rand;
		CacModVariables.MapVariables.get(world).Dat_time_gameplay = (CacModVariables.MapVariables.get(world).TimR_que_end - CacModVariables.MapVariables.get(world).TimR_que_start) / 20;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
