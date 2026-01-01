package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class TutoCheckpointEndProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MeowMoveOffProcedure.execute();
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
			}
		}
		CacModVariables.Tuto_score_running = false;
		CacModVariables.Tuto_checkpoint_index = 0;
		if (CacModVariables.Tuto_score >= 0) {
			CacModVariables.Msg_actionbar_text = "\u00A7e\uC131\uACF5!\u00A7r";
			net.owo.cac.CstTutorial.completeMission(2, true);
		} else {
			CacModVariables.Msg_actionbar_text = "\u00A7e\uC2E4\uD328...\u00A7r";
			net.owo.cac.CstTutorial.completeMission(2, false);
		}
	}
}
