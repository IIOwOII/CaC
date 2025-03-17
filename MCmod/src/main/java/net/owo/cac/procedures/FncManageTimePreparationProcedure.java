package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.nbt.IntTag;

public class FncManageTimePreparationProcedure {
	public static double execute(LevelAccessor world) {
		RandomSource rand;
		double time_preparation = 0;
		rand = RandomSource.create();
		time_preparation = Mth.nextInt(rand, 4, 6);
		CacModVariables.MapVariables.get(world).Dat_time_preparation.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) time_preparation));
		return time_preparation;
	}
}
