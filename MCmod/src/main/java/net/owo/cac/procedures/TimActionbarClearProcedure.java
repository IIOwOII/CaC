package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TimActionbarClearProcedure {
	public static void execute() {
		CacModVariables.Msg_actionbar_text = "";
		CacModVariables.Msg_actionbar_switch = false;
	}
}
