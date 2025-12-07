package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurrenderProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Exp_phase = 3.9;
		if (world.isClientSide()) {
			net.owo.cac.CstSurrender.startSurrender();
		}
	}
}
