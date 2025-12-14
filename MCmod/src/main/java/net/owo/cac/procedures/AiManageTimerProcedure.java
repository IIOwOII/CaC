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
		if (!world.isClientSide() && CacModVariables.Switch_AI) {
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
