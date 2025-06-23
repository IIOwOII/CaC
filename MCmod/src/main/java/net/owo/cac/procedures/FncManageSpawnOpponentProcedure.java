package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageSpawnOpponentProcedure {
	public static void execute(LevelAccessor world) {
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chased")) {
			CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = CacModVariables.MapVariables.get(world).Exp_trial % 4;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("pseudo_chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("pseudo_chased")) {
			CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = Math.floor(CacModVariables.MapVariables.get(world).Exp_trial * 2.5) % 4;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
