package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstRenderComponent;

public class PrdExperimentalProcedure {
	public static void execute() {
		CacModVariables.Exp_mode = "seeg";
		CstRenderComponent.renderPatchStart();
	}
}
