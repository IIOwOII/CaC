package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class IniPoolEventProcedure {
	public static void execute() {
		CacModVariables.Pool_event = new File((FMLPaths.GAMEDIR.get().toString() + "/cacutil/components"), File.separator + "pool_event.json");
	}
}
