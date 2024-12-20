package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.cac.network.CacModVariables;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double distance_player = 0;
		if (CacModVariables.MapVariables.get(world).Switch_Task && !entity.getPersistentData().getBoolean("C_Touch")) {
			if (entity.getPersistentData().getDouble("C_Timer") <= 0) {
				entity.getPersistentData().putDouble("C_Timer", 0.5);
				AiOpponentMoveProcedure.execute(world, x, y, z, entity);
			}
			entity.getPersistentData().putDouble("C_Timer", (entity.getPersistentData().getDouble("C_Timer") - 0.05));
			distance_player = Math.pow(Math.pow(x - CacModVariables.MapVariables.get(world).Pos_player_x, 2) + Math.pow(z - CacModVariables.MapVariables.get(world).Pos_player_z, 2), 0.5);
			if (distance_player < 1) {
				PrdTouchProcedure.execute(world, y, entity);
			}
		}
	}
}
