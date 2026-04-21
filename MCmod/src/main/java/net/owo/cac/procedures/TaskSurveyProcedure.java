package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TaskSurveyProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 3;
		net.owo.cac.CstSurvey.startSurvey();
	}
}
