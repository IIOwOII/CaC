package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.Entity;

public class TaskIntervalProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_phase = 4;
		CacModVariables.Switch_blank = true;
		FncManageIntervalProcedure.execute();
		EffRemoveMorphProcedure.execute(entity);
		RecManageProcedure.execute();
		net.owo.cac.CstRenderComponent.renderPatchReserved(40);
	}
}
