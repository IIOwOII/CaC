package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Switch_exp) {
			CacModVariables.MapVariables.get(world).Tim_trial_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			TaskPhaseEndProcedure.execute(world);
		}
	}
}
