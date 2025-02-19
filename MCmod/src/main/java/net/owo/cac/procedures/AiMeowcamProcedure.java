package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class AiMeowcamProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("timer_switch")) {
			entity.getPersistentData().putDouble("timer_time", (entity.getPersistentData().getDouble("timer_time") + 1));
			if (entity.getPersistentData().getDouble("timer_time") >= 100) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("####.##").format(entity.getPersistentData().getDouble("timer_time")))), false);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.####").format(entity.getX() - entity.getPersistentData().getDouble("pos_x")))), false);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.####").format(entity.getZ() - entity.getPersistentData().getDouble("pos_z")))), false);
				entity.getPersistentData().putBoolean("timer_switch", false);
			}
		}
	}
}
