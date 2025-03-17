package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

import java.util.Calendar;

public class TimAbsoluteSwitchProcedure {
	public static void execute(LevelAccessor world) {
		CacMod.LOGGER.info(Calendar.getInstance().getTime().toString());
		if (!CacModVariables.MapVariables.get(world).TimA_switch) {
			CacModVariables.MapVariables.get(world).TimA_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimA_tick = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimA_time_oldtick = Calendar.getInstance().getTimeInMillis();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimA_switch = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).TimA_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
