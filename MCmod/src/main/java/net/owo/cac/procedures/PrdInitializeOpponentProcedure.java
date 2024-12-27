package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdInitializeOpponentProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("C_Touch", false);
		entity.getPersistentData().putDouble("C_Timer", 0);
	}
}
