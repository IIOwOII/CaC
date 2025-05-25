package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.entity.EntPseudoMouseEntity;
import net.owo.cac.entity.EntMouseEntity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class AiMoveMouseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Vec3 field_obstacle = Vec3.ZERO;
		Vec3 field_player = Vec3.ZERO;
		Vec3 field_sum = Vec3.ZERO;
		Vec3 field_wall = Vec3.ZERO;
		Vec3 vec_destination = Vec3.ZERO;
		double speed = 0;
		field_player = FncFieldPlayerProcedure.execute(world, entity);
		field_obstacle = FncFieldObstacleProcedure.execute(world, entity);
		field_wall = FncFieldWallProcedure.execute(world, entity);
		field_sum = field_player.add((field_obstacle.add(field_wall)));
		vec_destination = (entity.position()).add(field_sum);
		if (entity instanceof EntMouseEntity) {
			speed = CacModVariables.MapVariables.get(world).Dat_difficulty_absolute;
			CacModVariables.MapVariables.get(world).Pos_opponent_destination = vec_destination;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (entity instanceof EntPseudoMouseEntity) {
			speed = 1;
			CacModVariables.MapVariables.get(world).Pos_player_destination = vec_destination;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((vec_destination.x()), (vec_destination.y()), (vec_destination.z()), (0.565685424949238 * Math.pow(speed, 0.5)));
	}
}
