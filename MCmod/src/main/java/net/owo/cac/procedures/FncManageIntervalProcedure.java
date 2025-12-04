package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

public class FncManageIntervalProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.Exp_session).equals("test_mixed") || (CacModVariables.Exp_session).equals("test_chasing") || (CacModVariables.Exp_session).equals("test_chased")) {
			CacModVariables.Dat_time_interval = 1;
		} else if ((CacModVariables.Exp_session).equals("pseudo_chasing") || (CacModVariables.Exp_session).equals("pseudo_chased")) {
			CacModVariables.Dat_time_interval = 1;
		}
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).TimR_que_time = CacModVariables.MapVariables.get(world).TimR_time + CacModVariables.Dat_time_interval * 20;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacMod.LOGGER.info(CacModVariables.MapVariables.get(world).TimR_que_time);
	}
}
