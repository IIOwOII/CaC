package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimEventCheckProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level());
		}
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Tim_event_pulse) {
			CacModVariables.MapVariables.get(world).Tim_event_occurpoint = CacModVariables.MapVariables.get(world).TimR_time + CacModVariables.MapVariables.get(world).Tim_event_duration;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tim_event_pulse = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Tim_event_occurpoint == CacModVariables.MapVariables.get(world).TimR_time) {
			CacMod.LOGGER.info("");
		}
	}
}
