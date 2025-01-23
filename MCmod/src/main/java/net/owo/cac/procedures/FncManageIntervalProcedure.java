package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageIntervalProcedure {
	public static double execute(LevelAccessor world) {
		double time_interval = 0;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test") || (CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			time_interval = 1;
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Option_tester_str = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Dat_time_interval.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(time_interval));
		return time_interval;
	}
}
