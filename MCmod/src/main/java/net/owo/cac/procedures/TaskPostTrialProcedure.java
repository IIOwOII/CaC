package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TaskPostTrialProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 5;
		CacModVariables.Exp_trial = CacModVariables.Exp_trial + 1;
		CacModVariables.Switch_blank = true;
		if (CacModVariables.Exp_trial >= CacModVariables.Exp_trial_total) {
			TaskPostRunProcedure.execute();
		}
	}
}
