package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TaskInterphaseProcedure {
	public static void execute(LevelAccessor world) {
		double session_type = 0;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chased")) {
			session_type = 0;
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			session_type = 1;
		}
	}
}
