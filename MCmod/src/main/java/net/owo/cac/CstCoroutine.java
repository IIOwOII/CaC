package net.owo.cac;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstState;

public class CstCoroutine {

	// Global variables
	public static boolean getSwitchTimelock() {
		return CacModVariables.Switch_timelock;
	}
	public static void setSwitchTimelock(boolean sw) {
		CacModVariables.Switch_timelock = sw;
	}
	public static String getEventQueue() {
		return CacModVariables.Ev_content;
	}

	// Set waiting
	public static void setWaiting() {
		int mode_code = CstState.getModeCode();
		if (mode_code != 1) return;
		String ev = getEventQueue();
		boolean sw = false;
		if (ev.endsWith("start")) {
			if ((ev).equals("main_start") || (ev).equals("fitting_start") || (ev).equals("test_start") || (ev).equals("pseudo_start")) {
				sw = true;
			}
		}
		else if (ev.startsWith("phase")) {
			if ((ev).equals("phase_pretrial")) {
				sw = true;
			}
		}
		setSwitchTimelock(sw);
	}
}
