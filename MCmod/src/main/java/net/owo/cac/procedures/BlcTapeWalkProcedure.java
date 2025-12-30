package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class BlcTapeWalkProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double id = 0;
		id = (double) net.owo.cac.CstTutorial.getTutorialID();
		if (id == 3 && CacModVariables.Tuto_score_running) {
			TutoRacingEndProcedure.execute(world, x, y, z);
		}
	}
}
