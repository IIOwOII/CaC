package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class EvQueImmediateProcedure {
	public static void execute() {
		CacModVariables.TimR_que_time = CacModVariables.TimR_time + 1;
	}
}
