package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TutoScoreTickProcedure {
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
		if (CacModVariables.Tuto_score_running && !world.isClientSide()) {
			if (CacModVariables.Tuto_hurdle_stack == CacModVariables.Tuto_hurdle_stack_old) {
				CacModVariables.Tuto_hurdle_stack = 0;
				CacModVariables.Tuto_hurdle_stack_old = 0;
				CacModVariables.Tuto_score = Math.round(CacModVariables.Tuto_score - 1);
				CacModVariables.Msg_actionbar_text = "Score : " + new java.text.DecimalFormat("#####").format(CacModVariables.Tuto_score);
			} else {
				CacModVariables.Tuto_hurdle_stack_old = CacModVariables.Tuto_hurdle_stack;
				CacModVariables.Tuto_score = Math.round(CacModVariables.Tuto_score - 4);
				CacModVariables.Msg_actionbar_text = "\u00A7cScore : " + new java.text.DecimalFormat("#####").format(CacModVariables.Tuto_score);
			}
		}
	}
}
