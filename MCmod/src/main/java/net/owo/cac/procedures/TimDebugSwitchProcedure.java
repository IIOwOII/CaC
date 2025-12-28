package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class TimDebugSwitchProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.TimD_switch) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eDebug Timer Off \u00A7r"), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eTime: \u00A7r" + new java.text.DecimalFormat("####").format(CacModVariables.TimD_time))), false);
			CacModVariables.TimD_switch = false;
			MeowMoveOffProcedure.execute();
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eDebug Timer On \u00A7r"), false);
			CacModVariables.TimD_time = 0;
			CacModVariables.TimD_switch = true;
			MeowMoveOnProcedure.execute();
		}
	}
}
