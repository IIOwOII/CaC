package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

public class FncManageSurveytypeProcedure {
	public static void execute(LevelAccessor world) {
		ListTag type_survey;
		type_survey = new ListTag();
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test")) {
			type_survey.addTag(0, IntTag.valueOf(-1));
		} else {
			type_survey.addTag(0, IntTag.valueOf(0));
			if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
				CacMod.LOGGER.info("temp");
			} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
				CacMod.LOGGER.info("temp");
			} else {
				CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
				CacModVariables.MapVariables.get(world).syncData(world);
				CacErrorProcedure.execute(world);
			}
			if (CacModVariables.MapVariables.get(world).Exp_trial % 10 == 9) {
				type_survey.addTag(type_survey.size(), IntTag.valueOf(5));
			}
		}
		CacModVariables.MapVariables.get(world).Dat_type_survey.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, (type_survey.copy()));
	}
}
