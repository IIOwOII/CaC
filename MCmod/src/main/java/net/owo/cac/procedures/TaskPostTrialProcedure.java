package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class TaskPostTrialProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Exp_phase = 4;
		CacModVariables.MapVariables.get(world).syncData(world);
		RecTrialresultProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Exp_trial = CacModVariables.MapVariables.get(world).Exp_trial + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Exp_trial < CacModVariables.MapVariables.get(world).Exp_trial_total) {
			TaskInterphaseProcedure.execute(world, x, y, z, entity);
		} else {
			TaskPostRunProcedure.execute(world);
		}
	}
}
