package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double distance_player = 0;
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			if (entity.getPersistentData().getDouble("C_Timer") <= 0) {
				entity.getPersistentData().putDouble("C_Timer", 0.5);
				AiOpponentMoveProcedure.execute(world, x, y, z, entity);
			}
			entity.getPersistentData().putDouble("C_Timer", (entity.getPersistentData().getDouble("C_Timer") - 0.05));
			distance_player = Math.pow(Math.pow(x - CacModVariables.MapVariables.get(world).Pos_player_x, 2) + Math.pow(z - CacModVariables.MapVariables.get(world).Pos_player_z, 2), 0.5);
			if (distance_player < 0.5) {
				if (world.isClientSide()) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.HOSTILE, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.HOSTILE, 1, 1, false);
						}
					}
				}
				PrdTouchProcedure.execute(world);
			}
		} else {
			if (entity instanceof Mob _entity)
				_entity.getNavigation().stop();
		}
	}
}
