package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Switch_exp) {
			CacModVariables.MapVariables.get(world).Tim_trial_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			TaskGameplayEndProcedure.execute(world, entity);
		}
	}
}
