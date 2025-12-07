package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class RtnSurrenderValueRightProcedure {
	public static boolean execute() {
		return CacModVariables.Dat_survey_surrender == 1;
	}
}
