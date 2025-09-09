package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.owo.cac.CstTutorial;

public class TutoResetProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		AdpResetProcedure.execute(world, entity);
		CstTutorial.resetTutorial();
	}
}
