package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

public class RtnStageRestProcedure {
	public static boolean execute(LevelAccessor world) {
		return CacModVariables.MapVariables.get(world).Exp_Rest;
	}
}
