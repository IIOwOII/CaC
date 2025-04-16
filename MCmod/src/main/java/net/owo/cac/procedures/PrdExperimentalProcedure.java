package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_pool = new com.google.gson.JsonObject();
		if (!world.isClientSide()) {
			CacModVariables.MapVariables.get(world).Exp_signal = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
