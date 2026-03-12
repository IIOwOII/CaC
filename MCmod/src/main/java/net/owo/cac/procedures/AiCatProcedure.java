package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.Switch_AI && !world.isClientSide()) {
			if (net.owo.cac.CstAgent.TimP_sample == 3) {
				AiMoveCatProcedure.execute(entity);
			}
			if ((CacModVariables.Pos_opponent_destination.subtract((entity.position()))).length() < 1 && (CacModVariables.Pos_opponent_destination.subtract((entity.position()))).length() > 0.5) {
				entity.setDeltaMovement((((CacModVariables.Pos_opponent_destination.subtract((entity.position()))).normalize()).scale((0.081649658 * CacModVariables.Dat_difficulty))));
			}
		}
	}
}
