package net.owo.cac.procedures;

import net.owo.cac.CstState;

public class PrdMeowViewOnProcedure {
	public static void execute() {
		if (!CstState.getMeowView()) {
			CstState.switchMeowView();
		}
	}
}
