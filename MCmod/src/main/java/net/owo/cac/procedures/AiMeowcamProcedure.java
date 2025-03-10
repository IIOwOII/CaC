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
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("(dx,dz)=(" + new java.text.DecimalFormat("##.########").format(entity.getX() - entity.getPersistentData().getDouble("pos_x")) + ","
						+ new java.text.DecimalFormat("##.########").format(entity.getZ() - entity.getPersistentData().getDouble("pos_z")) + ")")), false);
			entity.getPersistentData().putDouble("pos_x", (entity.getX()));
			entity.getPersistentData().putDouble("pos_z", (entity.getZ()));
			if (entity.getPersistentData().getDouble("timer_time") == 100) {
				entity.getPersistentData().putBoolean("timer_switch", false);
			}
		}
	}
}
