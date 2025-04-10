package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

public class FncFieldPlayerProcedure {
	public static Vec3 execute(LevelAccessor world) {
		double sca_K = 0;
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_PO = Vec3.ZERO;
		sca_K = 10;
		vec_PO = CacModVariables.MapVariables.get(world).Pos_opponent.subtract(CacModVariables.MapVariables.get(world).Pos_player);
		vec_field = (vec_PO.normalize()).scale((sca_K / vec_PO.length()));
		return vec_field;
	}
}
