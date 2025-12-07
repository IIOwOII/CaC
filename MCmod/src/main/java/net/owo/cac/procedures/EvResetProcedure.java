package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class EvResetProcedure {
	public static void execute() {
		CacModVariables.Switch_que = false;
		CacModVariables.Ev_occuring = false;
		CacModVariables.Ev_que_loop = false;
		CacModVariables.Ev_que_index = 0;
		CacModVariables.Ev_content = "";
		CacModVariables.Ev_pulse_content = "";
	}
}
