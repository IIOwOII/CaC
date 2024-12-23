package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Option_hotbar) {
			return;
		}
	}
}
