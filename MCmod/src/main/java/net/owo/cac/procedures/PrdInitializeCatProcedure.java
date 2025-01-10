package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdInitializeCatProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		PrdInitializeOpponentProcedure.execute(entity);
		entity.getPersistentData().putString("C_Name", "Predator");
		entity.getPersistentData().putDouble("K_Player", (-30));
		entity.getPersistentData().putDouble("K_Obstacle", 1);
		entity.getPersistentData().putDouble("K_Wall", 1);
	}
}
