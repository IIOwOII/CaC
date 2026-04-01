package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class DemoStartProcedure {
	public static void execute() {
		CacModVariables.Switch_AI = false;
		CacModVariables.Switch_blank = false;
		CacModVariables.Switch_trace = false;
		CacModVariables.Exp_trial = 0;
		CacModVariables.Exp_phase = 0;
		CacModVariables.Dat_trial_winlose = 0;
		CacModVariables.Dat_time_gameplay = 0;
		FncResetDatPosProcedure.execute();
	}
}
