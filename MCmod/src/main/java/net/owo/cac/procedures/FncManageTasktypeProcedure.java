package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageTasktypeProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.Exp_session).equals("test_mixed")) {
			CacModVariables.Dat_trial_type = CacModVariables.Exp_trial % 2;
		} else if ((CacModVariables.Exp_session).equals("chasing") || (CacModVariables.Exp_session).equals("simulation_chasing") || (CacModVariables.Exp_session).equals("test_chasing") || (CacModVariables.Exp_session).equals("pseudo_chasing")) {
			CacModVariables.Dat_trial_type = 0;
		} else if ((CacModVariables.Exp_session).equals("chased") || (CacModVariables.Exp_session).equals("simulation_chased") || (CacModVariables.Exp_session).equals("test_chased") || (CacModVariables.Exp_session).equals("pseudo_chased")) {
			CacModVariables.Dat_trial_type = 1;
		} else if ((CacModVariables.Exp_session).equals("fitting") || (CacModVariables.Exp_session).equals("simulation_fit_chasing") || (CacModVariables.Exp_session).equals("simulation_fit_chased")) {
			if ((CacModVariables.Psy_task).equals("chasing")) {
				CacModVariables.Dat_trial_type = 0;
			} else if ((CacModVariables.Psy_task).equals("chased")) {
				CacModVariables.Dat_trial_type = 1;
			}
		} else {
			CacModVariables.Log_error = "invalid_session";
			CacErrorProcedure.execute(world);
		}
	}
}
