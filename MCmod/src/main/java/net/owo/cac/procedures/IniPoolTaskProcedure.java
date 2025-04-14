package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.File;

public class IniPoolTaskProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Pool_task = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_task.json");
	}
}
