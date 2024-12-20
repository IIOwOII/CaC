package net.mcreator.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdInitializeMouseProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putString("C_Name", "Prey");
		entity.getPersistentData().putDouble("K_Player", 50);
		entity.getPersistentData().putDouble("K_Obstacle", 3);
		entity.getPersistentData().putDouble("K_Wall", 10);
		PrdInitializeOpponentProcedure.execute(entity);
	}
}
