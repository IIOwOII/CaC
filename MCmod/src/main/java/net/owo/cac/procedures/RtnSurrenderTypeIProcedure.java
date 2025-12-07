package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class RtnSurrenderTypeIProcedure {
	public static boolean execute() {
		return CacModVariables.Dat_survey_surrender_type == 0;
	}
}
