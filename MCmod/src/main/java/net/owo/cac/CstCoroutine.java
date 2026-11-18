package net.owo.cac;

import net.owo.cac.network.CacModVariables;

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
		String ev = getEventQueue();
		boolean sw = false;
		if (ev.startsWith("phase") {
			if ((ev).equals("phase_pretrial")) {
				sw = true;
			} else if ((ev).equals("phase_preparation")) {
				sw = true;
			} else if ((ev).equals("phase_gameplay")) {
				sw = true;
			} else if ((ev).equals("phase_surrender")) {
				sw = true;
			} else if ((ev).equals("phase_interval")) {
				sw = true;
			}
		}
		setSwitchTimelock(sw);
	}
}
