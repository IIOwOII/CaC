package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

public class FncManageSurveyResetProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dat_survey_time = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_answer = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_surrender = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		for (int index0 = 0; index0 < CacModVariables.MapVariables.get(world).Suv_reference.size(); index0++) {
			CacModVariables.MapVariables.get(world).Dat_survey_time.addTag(0, IntTag.valueOf(0));
			CacModVariables.MapVariables.get(world).Dat_survey_answer.addTag(0, IntTag.valueOf(0));
		}
	}
}
