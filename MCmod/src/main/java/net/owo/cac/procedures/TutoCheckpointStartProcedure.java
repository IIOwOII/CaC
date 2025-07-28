package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TutoCheckpointStartProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Tuto_checkpoint_index = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_content = "tutorial_checkpoint";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
