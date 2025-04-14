package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.File;

public class IniPoolEventProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Pool_event = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_event.json");
	}
}
