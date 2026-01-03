package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class AiStartProcedure {
	public static void execute() {
		net.owo.cac.CstAgent.setDuration(600);
		CacModVariables.Switch_AI = true;
		MeowMoveOnProcedure.execute();
	}
}
