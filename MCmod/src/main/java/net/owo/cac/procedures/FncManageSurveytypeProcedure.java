package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

public class FncManageSurveytypeProcedure {
	public static void execute(LevelAccessor world) {
		double idx_survey = 0;
		if (CacModVariables.MapVariables.get(world).Exp_trial == 0) {
			CacModVariables.MapVariables.get(world).Dat_survey_answer_pre = CacModVariables.MapVariables.get(world).List_survey_initial.copy();
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		CacModVariables.MapVariables.get(world).Dat_survey_order = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		idx_survey = 0;
		for (int index0 = 0; index0 < CacModVariables.MapVariables.get(world).List_survey_name.size(); index0++) {
			CacModVariables.MapVariables.get(world).Dat_survey_order.addTag((int) idx_survey, IntTag.valueOf((int) idx_survey));
			idx_survey = idx_survey + 1;
		}
	}
}
