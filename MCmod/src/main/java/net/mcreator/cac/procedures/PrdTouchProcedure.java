package net.mcreator.cac.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.CacMod;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world, double y, Entity entity) {
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
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(CacModVariables.MapVariables.get(world).Pos_player_x, y, CacModVariables.MapVariables.get(world).Pos_player_z),
							ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("intentionally_empty")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(CacModVariables.MapVariables.get(world).Pos_player_x, y, CacModVariables.MapVariables.get(world).Pos_player_z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("intentionally_empty")),
							SoundSource.NEUTRAL, 1, 1, false);
				}
			}
			CacMod.queueServerWork(100, () -> {
				PrdStageRestProcedure.execute(world);
				if (!entity.level().isClientSide())
					entity.discard();
			});
		}
	}
}
