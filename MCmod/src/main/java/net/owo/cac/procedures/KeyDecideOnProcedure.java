package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class KeyDecideOnProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Switch_survey
				|| CacModVariables.MapVariables.get(world).Switch_surrender && (CacModVariables.MapVariables.get(world).Dat_survey_surrender == 0 || CacModVariables.MapVariables.get(world).Dat_survey_surrender == 1)) {
			if (!world.isClientSide()) {
				CacModVariables.MapVariables.get(world).Ev_pulse_content = "decide";
				CacModVariables.MapVariables.get(world).syncData(world);
				EvPulseRecordProcedure.execute(world);
				EvQueImmediateProcedure.execute(world);
			}
		}
	}
}
