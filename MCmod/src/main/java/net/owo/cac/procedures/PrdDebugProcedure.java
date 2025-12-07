package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdDebugProcedure {
	public static void execute(LevelAccessor world) {
		String msg = "";
		CacModVariables.Switch_debug = !CacModVariables.Switch_debug;
		if (CacModVariables.Switch_debug) {
			msg = "\u00A7eDebug Mod On \u00A7r";
		} else {
			msg = "\u00A7eDebug Mod Off \u00A7r";
		}
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(msg), false);
	}
}
