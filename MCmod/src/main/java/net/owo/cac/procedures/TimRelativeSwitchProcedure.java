package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TimRelativeSwitchProcedure {
	public static void execute(LevelAccessor world) {
		if (!CacModVariables.MapVariables.get(world).TimR_switch) {
			CacModVariables.MapVariables.get(world).TimR_que_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimR_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimR_que_start = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimR_que_end = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimR_switch = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).TimR_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
