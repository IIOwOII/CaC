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
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7cNo file!"), false);
		} else if ((log_error).equals("nonexist_trialtype")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7cNo trial type!"), false);
		} else if ((log_error).equals("invalid_session")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7cInvalid session!"), false);
		}
		CacModVariables.MapVariables.get(world).Log_error = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
