package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.nbt.IntTag;

public class FncManageTimepoolPreparationProcedure {
	public static double execute(LevelAccessor world) {
		RandomSource rand;
		double time_preparation = 0;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test")) {
			rand = RandomSource.create();
			time_preparation = Mth.nextInt(rand, 4, 6);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Dat_time_preparation.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) time_preparation));
		return time_preparation;
	}
}
