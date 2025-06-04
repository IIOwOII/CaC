package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonArray arr_test = new com.google.gson.JsonArray();
		arr_test.add("1");
		arr_test.add("2");
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(arr_test.get(0).getAsString()), false);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(arr_test.get(1).getAsString()), false);
	}
}
