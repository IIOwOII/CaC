package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;

public class EffTickMorphProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setInvisible(true);
	}
}
