package net.owo.cac.procedures;

import net.owo.cac.world.inventory.GuiBlankMenu;
import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.ListTag;

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
		ListTag lst_survey;
		double idx_survey = 0;
		if (CacModVariables.MapVariables.get(world).Tim_survey_switch) {
			if (CacModVariables.MapVariables.get(world).Tim_survey_time == -1) {
				lst_survey = (CacModVariables.MapVariables.get(world).Dat_type_survey.get((int) CacModVariables.MapVariables.get(world).Exp_trial)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
				CacModVariables.MapVariables.get(world).Tim_survey_time = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				idx_survey = -1;
			}
			CacModVariables.MapVariables.get(world).Tim_survey_time = CacModVariables.MapVariables.get(world).Tim_survey_time + 0.05;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Tim_survey_time >= 5 && idx_survey == -1) {
				CacModVariables.MapVariables.get(world).Tim_survey_time = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				idx_survey = 0;
				if (entity instanceof Player _plr3 && _plr3.containerMenu instanceof GuiBlankMenu) {
					if (entity instanceof Player _player)
						_player.closeContainer();
				}
				CacModVariables.MapVariables.get(world).Tim_survey_switch = false;
				CacModVariables.MapVariables.get(world).syncData(world);
				TaskPhaseEndProcedure.execute(world);
			}
		}
	}
}
