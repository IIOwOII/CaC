package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class KeyDecideOnProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Switch_survey) {
			CacModVariables.MapVariables.get(world).Ev_pulse_content = "Decide";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvPulseRecordProcedure.execute(world);
			EvQueImmediateProcedure.execute(world);
		}
	}
}
