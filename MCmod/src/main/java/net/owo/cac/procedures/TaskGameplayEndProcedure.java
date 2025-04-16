package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class TaskGameplayEndProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Exp_phase = 2.5;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_trace = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_time_gameplay = CacModVariables.MapVariables.get(world).TimR_time - CacModVariables.MapVariables.get(world).Dat_time_gameplay;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Dat_trial_type == 0 && CacModVariables.MapVariables.get(world).Dat_time_gameplay < 600
				|| CacModVariables.MapVariables.get(world).Dat_trial_type == 1 && CacModVariables.MapVariables.get(world).Dat_time_gameplay >= 600) {
			CacModVariables.MapVariables.get(world).Dat_trial_winlose = 1;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Dat_trial_winlose = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		}
		EffApplyStopMoveProcedure.execute(entity);
	}
}
