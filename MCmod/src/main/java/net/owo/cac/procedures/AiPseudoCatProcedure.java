package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiPseudoCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.Switch_AI && !world.isClientSide()) {
			if (CacModVariables.TimP_sampling == 0) {
				AiMovePseudoCatProcedure.execute(entity);
			}
			if ((CacModVariables.Pos_player_destination.subtract((entity.position()))).length() <= 1 && (CacModVariables.Pos_player_destination.subtract((entity.position()))).length() > 0.5) {
				entity.setDeltaMovement((((CacModVariables.Pos_player_destination.subtract((entity.position()))).normalize()).scale((0.094280904 * CacModVariables.Dat_difficulty))));
			}
		}
	}
}
