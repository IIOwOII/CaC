package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageDifficultyProcedure {
	public static void execute(LevelAccessor world) {
		double difficulty_absolute = 0;
		double difficulty_relative = 0;
		String session = "";
		session = CacModVariables.MapVariables.get(world).Exp_session;
		if ((session).equals("test_mixed") || (session).equals("test_chasing") || (session).equals("test_chased")) {
			difficulty_absolute = 0.9 + 0.02 * (int) (CacModVariables.MapVariables.get(world).Exp_trial / 2);
			difficulty_relative = 0.9 + 0.02 * (int) (CacModVariables.MapVariables.get(world).Exp_trial / 2);
		}
		CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = difficulty_absolute;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_relative = difficulty_relative;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
