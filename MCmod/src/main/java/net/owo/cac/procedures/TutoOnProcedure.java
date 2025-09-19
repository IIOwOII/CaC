package net.owo.cac.procedures;

import net.owo.cac.CstTutorial;
import net.owo.cac.CstState;

public class TutoOnProcedure {
	public static void execute() {
		CstState.IsMeowView = true;
		CstState.CanMeowMove = true;
		CstTutorial.is_tutorial = true;
		CstTutorial.updateTutoQue();
	}
}
