package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		double tps = 0;
		tps = net.owo.cac.CstTimer.getTPS();
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.##").format(tps))), false);
	}
}
