package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

public class FncManageIntervalProcedure {
	public static void execute() {
		if (CacModVariables.Exp_session.startsWith("test") || CacModVariables.Exp_session.startsWith("pseudo")) {
			CacModVariables.Dat_time_interval = 1;
		} else if (CacModVariables.Exp_session.startsWith("fitting") || (CacModVariables.Exp_session).equals("chasing") || (CacModVariables.Exp_session).equals("chased")) {
			CacModVariables.Dat_time_interval = 3;
		}
		CacModVariables.Switch_blank = true;
		CacModVariables.TimR_que_time = CacModVariables.TimR_time + CacModVariables.Dat_time_interval * 20;
		CacMod.LOGGER.info(CacModVariables.TimR_que_time);
	}
}
