package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

public class PrdHotbarSwitchProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Option_hotbar) {
			CacModVariables.MapVariables.get(world).Option_hotbar = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Option_hotbar = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
