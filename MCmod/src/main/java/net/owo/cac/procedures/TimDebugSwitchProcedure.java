package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class TimDebugSwitchProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).TimD_switch) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eDebug Timer Off \u00A7r"), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eTime: \u00A7r" + new java.text.DecimalFormat("####").format(CacModVariables.MapVariables.get(world).TimD_time))), false);
			CacModVariables.MapVariables.get(world).TimD_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eDebug Timer On \u00A7r"), false);
			CacModVariables.MapVariables.get(world).TimD_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimD_switch = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
