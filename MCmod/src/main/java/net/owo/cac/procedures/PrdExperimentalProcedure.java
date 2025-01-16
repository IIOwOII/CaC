package net.owo.cac.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class PrdExperimentalProcedure {
	public static void execute(double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((x + 4), y, (z + 1), 0.2);
	}
}
