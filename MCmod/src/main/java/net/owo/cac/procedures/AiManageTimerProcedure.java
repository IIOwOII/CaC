package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AiManageTimerProcedure {
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
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			CacModVariables.MapVariables.get(world).Time_AI = CacModVariables.MapVariables.get(world).Time_AI + 1;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			if (CacModVariables.MapVariables.get(world).Time_AI != 0) {
				CacModVariables.MapVariables.get(world).Time_AI = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}
}
