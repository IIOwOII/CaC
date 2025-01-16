package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ByteTag;

public class RecTrialResultProcedure {
	public static void execute(LevelAccessor world) {
		boolean is_win = false;
		double type_trial = 0;
		type_trial = (CacModVariables.MapVariables.get(world).Dat_type_trial.get((int) CacModVariables.MapVariables.get(world).Exp_trial)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		if (type_trial == 0) {
			if (CacModVariables.MapVariables.get(world).Tim_trial_time < 30) {
				is_win = true;
			} else {
				is_win = false;
			}
		} else if (type_trial == 1) {
			if (CacModVariables.MapVariables.get(world).Tim_trial_time < 30) {
				is_win = false;
			} else {
				is_win = true;
			}
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "nonexist_trialtype";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		CacModVariables.MapVariables.get(world).Dat_time_gameplay.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, DoubleTag.valueOf(CacModVariables.MapVariables.get(world).Tim_trial_time));
		CacModVariables.MapVariables.get(world).Dat_win.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, ByteTag.valueOf(is_win));
	}
}
