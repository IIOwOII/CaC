package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, double x, double z, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			if (entity.getPersistentData().getDouble("C_Timer") <= 0) {
				entity.getPersistentData().putDouble("C_Timer", 0.5);
				AiOpponentMoveProcedure.execute(world, x, z, entity);
			}
			entity.getPersistentData().putDouble("C_Timer", (entity.getPersistentData().getDouble("C_Timer") - 0.05));
		}
	}
}
