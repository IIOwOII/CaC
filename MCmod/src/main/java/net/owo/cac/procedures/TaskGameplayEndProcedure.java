package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstRenderComponent;

public class TaskGameplayEndProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 2.5;
		if ((CacModVariables.Exp_mode).equals("seeg")) {
			CstRenderComponent.renderPatchToggle();
		}
	}
}
