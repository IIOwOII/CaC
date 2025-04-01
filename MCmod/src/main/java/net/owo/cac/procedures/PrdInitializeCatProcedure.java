package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdInitializeCatProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putString("C_Name", "Predator");
		entity.getPersistentData().putDouble("C_Timer", 0);
		entity.getPersistentData().putDouble("K_Player", (-10));
		entity.getPersistentData().putDouble("K_Obstacle", 0);
		entity.getPersistentData().putDouble("K_Wall", 0);
	}
}
