package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

public class FncManageIntervalProcedure {
	public static void execute() {
		if ((CacModVariables.Exp_session).equals("test_mixed") || (CacModVariables.Exp_session).equals("test_chasing") || (CacModVariables.Exp_session).equals("test_chased")) {
			CacModVariables.Dat_time_interval = 1;
		} else if ((CacModVariables.Exp_session).equals("pseudo_chasing") || (CacModVariables.Exp_session).equals("pseudo_chased")) {
			CacModVariables.Dat_time_interval = 1;
		} else if ((CacModVariables.Exp_session).equals("fitting")) {
			CacModVariables.Dat_time_interval = 1;
		}
		CacModVariables.Switch_blank = true;
		CacModVariables.TimR_que_time = CacModVariables.TimR_time + CacModVariables.Dat_time_interval * 20;
		CacMod.LOGGER.info(CacModVariables.TimR_que_time);
	}
}
