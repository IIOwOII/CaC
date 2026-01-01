package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TutoRacingStartProcedure {
	public static void execute() {
		MeowMoveOnProcedure.execute();
		CacModVariables.Tuto_score_running = true;
		net.owo.cac.CstTutorial.racingTutorial();
	}
}
