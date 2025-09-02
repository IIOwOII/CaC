package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TutoScoreTickProcedure {
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
		if (CacModVariables.MapVariables.get(world).Tuto_score_running) {
			if (CacModVariables.MapVariables.get(world).Tuto_hurdle_stack == CacModVariables.MapVariables.get(world).Tuto_hurdle_stack_old) {
				CacModVariables.MapVariables.get(world).Tuto_hurdle_stack = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Tuto_hurdle_stack_old = 0;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Tuto_score = Math.round(CacModVariables.MapVariables.get(world).Tuto_score - 1);
				CacModVariables.MapVariables.get(world).syncData(world);
			} else {
				CacModVariables.MapVariables.get(world).Tuto_hurdle_stack_old = CacModVariables.MapVariables.get(world).Tuto_hurdle_stack;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Tuto_score = Math.round(CacModVariables.MapVariables.get(world).Tuto_score - 1);
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Score : " + new java.text.DecimalFormat("#####").format(CacModVariables.MapVariables.get(world).Tuto_score))), true);
		}
	}
}
