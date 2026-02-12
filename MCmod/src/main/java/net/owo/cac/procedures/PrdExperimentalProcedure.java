package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class PrdExperimentalProcedure {
	public static void execute() {
		CacModVariables.Switch_blank = !CacModVariables.Switch_blank;
	}
}
