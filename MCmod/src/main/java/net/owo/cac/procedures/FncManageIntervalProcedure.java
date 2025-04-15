package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageIntervalProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed")) {
			CacModVariables.MapVariables.get(world).Dat_time_interval = 60;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
