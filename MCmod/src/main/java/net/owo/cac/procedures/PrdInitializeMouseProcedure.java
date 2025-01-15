package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdInitializeMouseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putString("C_Name", "Prey");
		entity.getPersistentData().putDouble("K_Player", 10);
		entity.getPersistentData().putDouble("K_Obstacle", 3);
		entity.getPersistentData().putDouble("K_Wall", 10);
		PrdInitializeOpponentProcedure.execute(world, entity);
	}
}
