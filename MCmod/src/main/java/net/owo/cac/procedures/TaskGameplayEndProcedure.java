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
		CacModVariables.Exp_phase = 2.5;
		PrdMeowMoveOffProcedure.execute();
		CacModVariables.Switch_AI = false;
		CacModVariables.Switch_trace = false;
		CacModVariables.Dat_time_gameplay = CacModVariables.TimR_time - CacModVariables.Dat_time_gameplay;
		if (CacModVariables.Dat_trial_type == 0 && CacModVariables.Dat_time_gameplay < 600 || CacModVariables.Dat_trial_type == 1 && CacModVariables.Dat_time_gameplay >= 600) {
			CacModVariables.Dat_trial_winlose = 1;
		} else {
			CacModVariables.Dat_trial_winlose = 0;
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
