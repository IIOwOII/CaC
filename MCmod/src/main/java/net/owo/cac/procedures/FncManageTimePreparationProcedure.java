package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class FncManageTimePreparationProcedure {
	public static void execute(LevelAccessor world) {
		RandomSource rand;
		rand = RandomSource.create();
		CacModVariables.MapVariables.get(world).Dat_time_preparation = Mth.nextInt(rand, 4, 6);
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
