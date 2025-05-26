package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class AiMoveCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Vec3 vec_destination = Vec3.ZERO;
		Vec3 field_sum = Vec3.ZERO;
		Vec3 vec_P = Vec3.ZERO;
		Vec3 vec_P_prime = Vec3.ZERO;
		Vec3 vec_PP = Vec3.ZERO;
		field_sum = Vec3.ZERO;
		vec_P = entity.position();
		vec_P_prime = CacModVariables.MapVariables.get(world).Pos_player;
		vec_PP = vec_P_prime.subtract(vec_P);
		field_sum = field_sum.add((vec_PP.add(((vec_PP.normalize()).scale(1.726872)))));
		vec_destination = vec_P.add(field_sum);
		CacModVariables.MapVariables.get(world).Pos_opponent_destination = vec_destination;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((vec_destination.x()), (vec_destination.y()), (vec_destination.z()), (0.565685424949238 * Math.pow(CacModVariables.MapVariables.get(world).Dat_difficulty_absolute, 0.5)));
	}
}
