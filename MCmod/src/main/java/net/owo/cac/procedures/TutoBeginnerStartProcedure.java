package net.owo.cac.procedures;

public class TutoBeginnerStartProcedure {
	public static void execute() {
		MeowMoveOnProcedure.execute();
		net.owo.cac.CstTutorial.movingTutorial();
	}
}
