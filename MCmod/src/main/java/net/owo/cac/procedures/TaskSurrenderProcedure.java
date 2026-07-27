package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class TaskSurrenderProcedure {
	public static void execute() {
		CacModVariables.Exp_phase = 3.9;
		net.owo.cac.CstSurrender.startSurrender();
		net.owo.cac.CstRenderComponent.renderPatchReserved(39);
	}
}
