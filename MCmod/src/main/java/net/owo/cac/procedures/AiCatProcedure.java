package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.Switch_AI && !world.isClientSide()) {
			net.owo.cac.CstAgent.getPath(entity, true, true);
			if (net.owo.cac.CstAgent.TimP_sample == 3) {
				AiMoveCatProcedure.execute(entity);
			}
		}
	}
}
