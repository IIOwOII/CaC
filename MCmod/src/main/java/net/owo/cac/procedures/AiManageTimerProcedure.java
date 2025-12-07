package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AiManageTimerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event);
		}
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		if (CacModVariables.Switch_AI) {
			if ((CacModVariables.Pos_opponent.subtract(CacModVariables.Pos_player)).length() < 4) {
				CacModVariables.Time_AI = (CacModVariables.Time_AI + 1) % 5;
			} else {
				CacModVariables.Time_AI = (CacModVariables.Time_AI + 1) % 10;
			}
		} else {
			if (CacModVariables.Time_AI != 0) {
				CacModVariables.Time_AI = 0;
			}
		}
	}
}
