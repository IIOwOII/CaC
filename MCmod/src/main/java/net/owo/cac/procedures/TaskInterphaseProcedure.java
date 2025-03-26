package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class TaskInterphaseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double session_type = 0;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chased")) {
			session_type = 0;
		} else {
			session_type = 1;
		}
		if (CacModVariables.MapVariables.get(world).Exp_phase == 0) {
			if (session_type == 0) {
				TaskGameplayProcedure.execute(world, entity);
			} else if (session_type == 1) {
				TaskPreparationProcedure.execute(world, entity);
			}
		} else if (CacModVariables.MapVariables.get(world).Exp_phase == 1) {
			TaskGameplayProcedure.execute(world, entity);
		} else if (CacModVariables.MapVariables.get(world).Exp_phase == 2) {
			if (session_type == 0) {
				TaskSurveyProcedure.execute(world, x, y, z, entity);
			} else if (session_type == 1) {
				TaskPostTrialProcedure.execute(world, x, y, z, entity);
			}
		} else if (CacModVariables.MapVariables.get(world).Exp_phase == 3) {
			CacMod.LOGGER.info("temp");
		} else if (CacModVariables.MapVariables.get(world).Exp_phase == 4) {
			if (session_type == 0) {
				TaskPreTrialProcedure.execute(world, x, y, z, entity);
			} else if (session_type == 1) {
				CacMod.LOGGER.info("interval");
			}
		}
	}
}
