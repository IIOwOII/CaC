package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.Switch_AI && !world.isClientSide()) {
			if (net.owo.cac.CstAgent.TimP_sample == 1) {
				AiMoveMouseProcedure.execute(entity);
			}
		}
	}
}
