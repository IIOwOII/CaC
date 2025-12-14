package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!world.isClientSide()) {
			if (CacModVariables.Switch_AI && CacModVariables.Time_AI == 0) {
				AiMoveMouseProcedure.execute(world, entity);
			}
			if ((CacModVariables.Pos_opponent_destination.subtract((entity.position()))).length() <= 1 && (CacModVariables.Pos_opponent_destination.subtract((entity.position()))).length() > 0.5) {
				entity.setDeltaMovement((((CacModVariables.Pos_opponent_destination.subtract((entity.position()))).normalize()).scale((0.094280904 * CacModVariables.Dat_difficulty_absolute))));
			}
		}
	}
}
