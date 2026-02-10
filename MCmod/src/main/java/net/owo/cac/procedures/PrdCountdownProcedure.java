package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class PrdCountdownProcedure {
	public static void execute() {
		CacModVariables.TimC_time = 100;
		CacModVariables.TimC_que = "normal";
		CacModVariables.TimC_switch = true;
	}
}
