package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimSurveyProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Tim_survey_switch) {
			if (CacModVariables.MapVariables.get(world).Tim_survey_time < 0) {
				CacModVariables.MapVariables.get(world).Tim_survey_time = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Exp_survey_idx = -1;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			CacModVariables.MapVariables.get(world).Tim_survey_time = CacModVariables.MapVariables.get(world).Tim_survey_time + 0.05;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Tim_survey_time >= 5 && CacModVariables.MapVariables.get(world).Exp_survey_idx == -1) {
				CacModVariables.MapVariables.get(world).Tim_survey_time = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Exp_survey_idx = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				if (entity instanceof Player _player)
					_player.closeContainer();
				CacModVariables.MapVariables.get(world).Tim_survey_switch = false;
				CacModVariables.MapVariables.get(world).syncData(world);
				TaskPhaseEndProcedure.execute(world);
			}
		}
	}
}
