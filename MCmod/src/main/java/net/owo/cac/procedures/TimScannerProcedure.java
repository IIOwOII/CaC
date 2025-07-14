package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimScannerProcedure {
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
		if (CacModVariables.MapVariables.get(world).Switch_scanner && !world.isClientSide()) {
			if (CacModVariables.MapVariables.get(world).Exp_signal) {
				CacModVariables.MapVariables.get(world).TimS_time = CacModVariables.MapVariables.get(world).TimS_time + 1;
				CacModVariables.MapVariables.get(world).syncData(world);
				RecScannerProcedure.execute(world);
				CacModVariables.MapVariables.get(world).Exp_signal = false;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}
}
