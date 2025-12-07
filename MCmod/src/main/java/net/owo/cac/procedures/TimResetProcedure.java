package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import java.util.Calendar;

public class TimResetProcedure {
	public static void execute() {
		CacMod.LOGGER.info(Calendar.getInstance().getTime().toString());
		CacModVariables.TimA_time_oldtick = Calendar.getInstance().getTimeInMillis();
		CacModVariables.TimA_time = 0;
		CacModVariables.TimR_time = 0;
		CacModVariables.Ev_occuring = false;
		CacModVariables.Switch_que = false;
		CacModVariables.Switch_timer = true;
	}
}
