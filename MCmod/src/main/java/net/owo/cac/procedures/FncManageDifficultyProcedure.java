package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.DoubleTag;

public class FncManageDifficultyProcedure {
	public static void execute(LevelAccessor world) {
		double difficulty_absolute = 0;
		double difficulty_relative = 0;
		difficulty_absolute = 1;
		difficulty_relative = 1;
		CacMod.LOGGER.info("Temp!!!");
		CacModVariables.MapVariables.get(world).Exp_difficulty_absolute = difficulty_absolute;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_difficulty_relative = difficulty_relative;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_difficulty_absolute.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(difficulty_absolute));
		CacModVariables.MapVariables.get(world).Dat_trial_difficulty_relative.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(difficulty_relative));
	}
}
