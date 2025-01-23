package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class PrdExperimentalProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		TaskCleanMobProcedure.execute(entity);
	}
}
