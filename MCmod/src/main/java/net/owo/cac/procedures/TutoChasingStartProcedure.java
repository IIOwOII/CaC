package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TutoChasingStartProcedure {
	public static void execute() {
		CacModVariables.Switch_AI = true;
		MeowMoveOnProcedure.execute();
	}
}
