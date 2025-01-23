package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.IntTag;

public class FncManageTasktypeProcedure {
	public static double execute(LevelAccessor world) {
		double idx_type = 0;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test")) {
			idx_type = CacModVariables.MapVariables.get(world).Exp_trial % 2;
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing")) {
			idx_type = 0;
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			idx_type = 1;
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Dat_type_trial.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) idx_type));
		return idx_type;
	}
}
