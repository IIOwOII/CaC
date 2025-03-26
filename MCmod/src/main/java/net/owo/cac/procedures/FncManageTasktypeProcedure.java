package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

public class FncManageTasktypeProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed")) {
			CacModVariables.MapVariables.get(world).Dat_trial_type = CacModVariables.MapVariables.get(world).Exp_trial % 2;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_chasing")) {
			CacModVariables.MapVariables.get(world).Dat_trial_type = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_chased")) {
			CacModVariables.MapVariables.get(world).Dat_trial_type = 1;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
	}
}
