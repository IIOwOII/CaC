package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageDifficultyProcedure {
	public static void execute(LevelAccessor world) {
		double difficulty_absolute = 0;
		double difficulty_relative = 0;
		String session = "";
		session = CacModVariables.MapVariables.get(world).Exp_session;
		if ((session).equals("test_mixed") || (session).equals("test_chasing") || (session).equals("test_chased")) {
			difficulty_absolute = 0.8 + 0.02 * Math.floor(CacModVariables.MapVariables.get(world).Exp_trial / 2);
			difficulty_relative = 0.8 + 0.02 * Math.floor(CacModVariables.MapVariables.get(world).Exp_trial / 2);
		}
		CacModVariables.MapVariables.get(world).Exp_difficulty_absolute = difficulty_absolute;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_difficulty_relative = difficulty_relative;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_difficulty_absolute.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(difficulty_absolute));
		CacModVariables.MapVariables.get(world).Dat_trial_difficulty_relative.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(difficulty_relative));
	}
}
