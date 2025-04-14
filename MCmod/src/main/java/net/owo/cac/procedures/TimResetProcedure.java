package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

import java.util.Calendar;

public class TimResetProcedure {
	public static void execute(LevelAccessor world) {
		CacMod.LOGGER.info(Calendar.getInstance().getTime().toString());
		CacModVariables.MapVariables.get(world).TimA_time_oldtick = Calendar.getInstance().getTimeInMillis();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).TimA_time = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).TimR_time = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_occuring = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_que = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_timer = true;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
