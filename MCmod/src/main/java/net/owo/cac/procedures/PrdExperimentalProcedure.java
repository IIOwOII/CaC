package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonArray arr_test = new com.google.gson.JsonArray();
		IniLogProcedure.execute(world);
	}
}
