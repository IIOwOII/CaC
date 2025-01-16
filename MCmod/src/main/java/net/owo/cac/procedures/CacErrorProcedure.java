package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class CacErrorProcedure {
	public static void execute(LevelAccessor world) {
		String log_error = "";
		log_error = CacModVariables.MapVariables.get(world).Log_error;
		if ((log_error).equals("nonexist_file")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("No file!"), false);
		} else if ((log_error).equals("nonexist_trialtype")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("No trial type!"), false);
		}
		CacModVariables.MapVariables.get(world).Log_error = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
