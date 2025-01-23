package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdDebugProcedure {
	public static void execute(LevelAccessor world) {
		String msg = "";
		CacModVariables.MapVariables.get(world).Switch_debug = !CacModVariables.MapVariables.get(world).Switch_debug;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			msg = "\u00A7cDebug Mod On";
		} else {
			msg = "\u00A7cDebug Mod Off";
		}
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(msg), false);
	}
}
