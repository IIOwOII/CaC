package net.mcreator.cac.procedures;

import net.minecraft.world.entity.Entity;

public class RtnTouchProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity.getPersistentData().getBoolean("C_Touch");
	}
}
