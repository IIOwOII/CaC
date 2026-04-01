package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstState;

public class TaskPreRunProcedure {
	public static void execute() {
		CacModVariables.Switch_AI = false;
		CacModVariables.Switch_blank = false;
		CacModVariables.Switch_trace = false;
		CacModVariables.Exp_trial = 0;
		CacModVariables.Exp_phase = 0;
		CacModVariables.Dat_trial_type = 0;
		CacModVariables.Dat_difficulty = 1;
		CacModVariables.Dat_trial_spawnpoint_opponent = 0;
		CacModVariables.Dat_trial_winlose = 0;
		CacModVariables.Dat_time_preparation = 0;
		CacModVariables.Dat_time_gameplay = 0;
		CacModVariables.Dat_time_interval = 0;
		FncResetDatPosProcedure.execute();
		CstState.IsMeowView = true;
	}
}
