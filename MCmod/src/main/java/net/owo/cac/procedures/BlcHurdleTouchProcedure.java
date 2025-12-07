package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class BlcHurdleTouchProcedure {
	public static void execute() {
		CacModVariables.Tuto_hurdle_stack = CacModVariables.Tuto_hurdle_stack + 1;
	}
}
