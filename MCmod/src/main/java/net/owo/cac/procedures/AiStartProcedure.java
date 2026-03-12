package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class AiStartProcedure {
	public static void execute() {
		if ((CacModVariables.Exp_session).equals("simulation_chasing") || (CacModVariables.Exp_session).equals("simulation_chased")) {
			net.owo.cac.CstAgent.setDuration(1000);
		} else {
			net.owo.cac.CstAgent.setDuration(600);
		}
		CacModVariables.Switch_AI = true;
		MeowMoveOnProcedure.execute();
	}
}
