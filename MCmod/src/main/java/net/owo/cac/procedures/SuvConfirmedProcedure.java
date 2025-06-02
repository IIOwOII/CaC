package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class SuvConfirmedProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_phase = 3.9;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_index = Math.round(CacModVariables.MapVariables.get(world).Dat_survey_index + 1);
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Dat_survey_index < CacModVariables.MapVariables.get(world).List_survey_name.size()) {
			CacModVariables.MapVariables.get(world).Ev_content = "survey_waiting";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
