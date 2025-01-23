package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageSpeedProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test")) {
			CacModVariables.MapVariables.get(world).Pmt_difficulty = 0.6 + 0.02 * Math.floor(CacModVariables.MapVariables.get(world).Exp_trial / 5);
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing")) {
			CacMod.LOGGER.info("temp");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Dat_speed.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(CacModVariables.MapVariables.get(world).Pmt_difficulty));
	}
}
