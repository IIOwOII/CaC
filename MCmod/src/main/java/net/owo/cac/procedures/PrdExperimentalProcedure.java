package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class PrdExperimentalProcedure {
	public static void execute() {
		net.owo.cac.CstField.recObstacle();
		CacModVariables.Dir_behaviors = CacModVariables.Dir_behaviors + "" + CacModVariables.Ev_content;
	}
}
