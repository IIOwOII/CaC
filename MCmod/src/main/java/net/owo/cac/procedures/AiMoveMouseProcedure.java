package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstAgent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class AiMoveMouseProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		Vec3 vec_destination = Vec3.ZERO;
		vec_destination = net.owo.cac.CstAgent.destPrey(true);
		CacModVariables.Pos_opponent_destination = vec_destination;
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((vec_destination.x()), (vec_destination.y()), (vec_destination.z()), (0.48989794855 * Math.pow(CacModVariables.Dat_difficulty, 0.5)));
		CstAgent.getPath(entity, true);
	}
}
