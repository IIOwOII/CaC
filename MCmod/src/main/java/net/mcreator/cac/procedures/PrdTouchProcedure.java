package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.cac.CacMod;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!entity.getPersistentData().getBoolean("C_Touch")) {
			entity.getPersistentData().putBoolean("C_Touch", true);
			if ((entity.getPersistentData().getString("C_Name")).equals("Prey")) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("You catched!"), false);
			} else if ((entity.getPersistentData().getString("C_Name")).equals("Predator")) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("You got caught!"), false);
			}
			CacMod.queueServerWork(100, () -> {
				PrdStageRestProcedure.execute(world);
				if (!entity.level().isClientSide())
					entity.discard();
			});
		}
	}
}
