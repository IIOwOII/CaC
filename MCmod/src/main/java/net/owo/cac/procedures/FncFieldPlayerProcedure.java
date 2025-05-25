package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.entity.EntPseudoMouseEntity;
import net.owo.cac.entity.EntMouseEntity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class FncFieldPlayerProcedure {
	public static Vec3 execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return Vec3.ZERO;
		double sca_K = 0;
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_PO = Vec3.ZERO;
		Vec3 vec_PP = Vec3.ZERO;
		Vec3 vec_P = Vec3.ZERO;
		Vec3 vec_P_prime = Vec3.ZERO;
		sca_K = 15;
		vec_P = entity.position();
		if (entity instanceof EntMouseEntity) {
			vec_P_prime = CacModVariables.MapVariables.get(world).Pos_player;
		} else if (entity instanceof EntPseudoMouseEntity) {
			vec_P_prime = CacModVariables.MapVariables.get(world).Pos_opponent;
		}
		vec_PP = vec_P_prime.subtract(vec_P);
		vec_field = (vec_PP.normalize()).scale((-(sca_K / vec_PP.length())));
		return vec_field;
	}
}
