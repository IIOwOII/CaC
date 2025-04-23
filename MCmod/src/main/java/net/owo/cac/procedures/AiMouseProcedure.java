package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, double x, double z, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			if (CacModVariables.MapVariables.get(world).Time_AI == 0 && !world.isClientSide()) {
				AiOpponentMoveProcedure.execute(world, x, z, entity);
			}
		}
	}
}
