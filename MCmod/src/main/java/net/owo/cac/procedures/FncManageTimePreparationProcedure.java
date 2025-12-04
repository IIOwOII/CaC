package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageTimePreparationProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Dat_time_preparation = (CacModVariables.MapVariables.get(world).List_random_preparation.get((int) CacModVariables.Exp_trial)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).TimR_que_time = CacModVariables.MapVariables.get(world).TimR_time + CacModVariables.Dat_time_preparation * 20;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
