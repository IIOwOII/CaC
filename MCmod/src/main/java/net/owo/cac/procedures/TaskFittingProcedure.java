package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TaskFittingProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = -1;
		net.owo.cac.CstPsychometric.updateTrialBefore();
	}
}
