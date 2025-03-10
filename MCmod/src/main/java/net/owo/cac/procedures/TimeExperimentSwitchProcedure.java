package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

import java.util.Calendar;

public class TimeExperimentSwitchProcedure {
	public static void execute(LevelAccessor world) {
		if (CacModVariables.MapVariables.get(world).Tim_experiment_switch) {
			CacModVariables.MapVariables.get(world).Tim_experiment_time_end = Calendar.getInstance().getTimeInMillis();
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Switch_debug) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDatetime: \u00A7r" + Calendar.getInstance().getTime().toString())), false);
			}
		} else {
			CacModVariables.MapVariables.get(world).Tim_experiment_time_start = Calendar.getInstance().getTimeInMillis();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tim_experiment_time_oldtick = Calendar.getInstance().getTimeInMillis();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tim_experiment_tick = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tim_experiment_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		CacModVariables.MapVariables.get(world).Tim_experiment_switch = !CacModVariables.MapVariables.get(world).Tim_experiment_switch;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
