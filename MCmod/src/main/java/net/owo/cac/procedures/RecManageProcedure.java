package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class RecManageProcedure {
	public static void execute() {
		if (CacModVariables.Exp_property.contains("P")) {
			RecPositionProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("G")) {
			RecGameplayProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("C")) {
			RecScannerProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("F")) {
			net.owo.cac.CstPsychometric.updateTrialAfter();
		}
	}
}
