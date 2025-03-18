package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

import java.util.Calendar;

@Mod.EventBusSubscriber
public class TimAbsoluteProcedure {
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
		if (CacModVariables.MapVariables.get(world).TimA_switch) {
			CacModVariables.MapVariables.get(world).TimA_time_currtick = Calendar.getInstance().getTimeInMillis();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimA_time = CacModVariables.MapVariables.get(world).TimA_time + CacModVariables.MapVariables.get(world).TimA_time_currtick - CacModVariables.MapVariables.get(world).TimA_time_oldtick;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimA_time_oldtick = CacModVariables.MapVariables.get(world).TimA_time_currtick;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Switch_debug) {
				CacMod.LOGGER.info(Calendar.getInstance().getTime().toString());
			}
		}
	}
}
