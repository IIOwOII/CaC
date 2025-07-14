package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class SimIntervalProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		EffRemoveMorphProcedure.execute(entity);
		RecPositionProcedure.execute(world);
		RecGameplayProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Exp_trial = CacModVariables.MapVariables.get(world).Exp_trial + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Exp_trial >= CacModVariables.MapVariables.get(world).Exp_trial_total) {
			TaskPostRunProcedure.execute(world);
		}
	}
}
