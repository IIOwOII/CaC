package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class TaskIntervalProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_phase = 4;
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		FncManageIntervalProcedure.execute(world);
		EffRemoveMorphProcedure.execute(entity);
		RecGameplayProcedure.execute();
		RecPositionProcedure.execute();
		RecSurveyProcedure.execute(world);
	}
}
