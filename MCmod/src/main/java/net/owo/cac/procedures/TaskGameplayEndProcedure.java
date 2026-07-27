package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TaskGameplayEndProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 2.5;
		net.owo.cac.CstRenderComponent.renderPatchReserved(25);
	}
}
