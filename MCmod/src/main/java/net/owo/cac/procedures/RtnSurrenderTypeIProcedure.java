package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class RtnSurrenderTypeIProcedure {
	public static boolean execute(LevelAccessor world) {
		return CacModVariables.MapVariables.get(world).Dat_survey_surrender_type == 0;
	}
}
