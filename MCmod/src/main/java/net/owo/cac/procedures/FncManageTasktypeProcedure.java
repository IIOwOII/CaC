package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageTasktypeProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.Exp_session).equals("test_mixed")) {
			CacModVariables.Dat_trial_type = CacModVariables.Exp_trial % 2;
		} else if (CacModVariables.Exp_session.endsWith("chasing")) {
			CacModVariables.Dat_trial_type = 0;
		} else if (CacModVariables.Exp_session.endsWith("chased")) {
			CacModVariables.Dat_trial_type = 1;
		} else {
			CacModVariables.Log_error = "invalid_session";
			CacErrorProcedure.execute(world);
		}
	}
}
