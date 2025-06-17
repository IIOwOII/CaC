package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class FncManageDifficultyProcedure {
	public static void execute(LevelAccessor world) {
		String session = "";
		double difficulty_absolute = 0;
		double difficulty_relative = 0;
		session = CacModVariables.MapVariables.get(world).Exp_session;
		if ((session).equals("test_mixed")) {
			difficulty_absolute = Math.round(Math.pow(10, 2) * (0.9 + 0.05 * Math.floor(CacModVariables.MapVariables.get(world).Exp_trial / 2))) / Math.pow(10, 2);
			difficulty_relative = difficulty_absolute;
		} else if ((session).equals("test_chasing") || (session).equals("test_chased")) {
			difficulty_absolute = Math.round(Math.pow(10, 2) * (0.9 + 0.05 * CacModVariables.MapVariables.get(world).Exp_trial)) / Math.pow(10, 2);
			difficulty_relative = difficulty_absolute;
		}
		CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = difficulty_absolute;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_relative = difficulty_relative;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
