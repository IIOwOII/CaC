package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Switch_AI && !world.isClientSide()) {
			if (CacModVariables.MapVariables.get(world).Time_AI == 0) {
				AiMoveMouseProcedure.execute(world, entity);
			}
			if ((CacModVariables.MapVariables.get(world).Pos_opponent_destination.subtract((entity.position()))).length() <= 1 && (CacModVariables.MapVariables.get(world).Pos_opponent_destination.subtract((entity.position()))).length() > 0.5) {
				entity.setDeltaMovement((((CacModVariables.MapVariables.get(world).Pos_opponent_destination.subtract((entity.position()))).normalize()).scale((0.094280904 * CacModVariables.MapVariables.get(world).Dat_difficulty_absolute))));
			}
		}
	}
}
