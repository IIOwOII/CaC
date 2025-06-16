package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskSurrenderEndProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_surrender = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Dat_survey_surrender_type == 1) {
			CacModVariables.MapVariables.get(world).Dat_survey_surrender = (CacModVariables.MapVariables.get(world).Dat_survey_surrender + 1) % 2;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
