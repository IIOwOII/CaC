package net.mcreator.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdInitializeCatProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putString("C_Name", "Predator");
		entity.getPersistentData().putBoolean("C_Touch", false);
	}
}
