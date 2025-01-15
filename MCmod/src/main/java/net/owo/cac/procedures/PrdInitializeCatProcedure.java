package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdInitializeCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		PrdInitializeOpponentProcedure.execute(world, entity);
		entity.getPersistentData().putString("C_Name", "Predator");
		entity.getPersistentData().putDouble("K_Player", (-30));
		entity.getPersistentData().putDouble("K_Obstacle", 1);
		entity.getPersistentData().putDouble("K_Wall", 1);
	}
}
