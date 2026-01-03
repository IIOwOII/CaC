package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class AiEndProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		MeowMoveOffProcedure.execute();
		CacModVariables.Switch_AI = false;
		CacModVariables.Switch_trace = false;
		CacModVariables.Dat_time_gameplay = net.owo.cac.CstAgent.getDuration();
		CacModVariables.Dat_trial_winlose = net.owo.cac.CstAgent.getResult();
		if (!world.isClientSide()) {
			if (CacModVariables.Dat_trial_winlose == 1) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_a7")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_a7")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
			} else if (CacModVariables.Dat_trial_winlose == 0) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_c7")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_c7")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
			}
		}
		EffApplyStopMoveProcedure.execute(entity);
	}
}
