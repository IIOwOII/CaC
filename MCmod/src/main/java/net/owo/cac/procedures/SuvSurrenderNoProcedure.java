package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class SuvSurrenderNoProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Dat_survey_surrender = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof Player _player)
			_player.closeContainer();
		CacModVariables.MapVariables.get(world).Ev_pulse_content = "vote_surrender";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvPulseRecordProcedure.execute(world);
		EvQueImmediateProcedure.execute(world);
		CacMod.LOGGER.info(0);
	}
}
