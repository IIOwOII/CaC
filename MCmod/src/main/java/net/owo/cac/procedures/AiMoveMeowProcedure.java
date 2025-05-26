package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AiMoveMeowProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Vec3 vec_PG = Vec3.ZERO;
		double len_PG = 0;
		vec_PG = CacModVariables.MapVariables.get(world).Meow_destination.subtract((entity.position()));
		len_PG = vec_PG.length();
		if (len_PG < 1 && len_PG > 0.5) {
			entity.setDeltaMovement(((vec_PG.normalize()).scale((0.094280904 * 1))));
		}
	}
}
