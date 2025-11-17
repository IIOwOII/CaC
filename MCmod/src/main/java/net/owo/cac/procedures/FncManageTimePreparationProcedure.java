package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageTimePreparationProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dat_time_preparation = 5;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).TimR_que_time = CacModVariables.MapVariables.get(world).TimR_time + CacModVariables.MapVariables.get(world).Dat_time_preparation * 20;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
