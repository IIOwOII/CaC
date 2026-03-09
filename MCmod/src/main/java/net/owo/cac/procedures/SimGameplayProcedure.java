package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class SimGameplayProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 2;
		CacModVariables.Switch_trace = true;
		AiStartProcedure.execute();
	}
}
