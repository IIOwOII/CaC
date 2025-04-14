package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageTimeGameplayProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dat_time_gameplay = (CacModVariables.MapVariables.get(world).TimR_que_time - CacModVariables.MapVariables.get(world).TimR_que_start) / 20;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
