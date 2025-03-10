package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdTouchMeowcamProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("timer_time", 0);
		entity.getPersistentData().putDouble("pos_x", (entity.getX()));
		entity.getPersistentData().putDouble("pos_z", (entity.getZ()));
		entity.getPersistentData().putBoolean("timer_switch", true);
	}
}
