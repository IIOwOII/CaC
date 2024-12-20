package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.cac.network.CacModVariables;

public class FncFieldPlayerCatProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double z, Entity entity) {
		if (entity == null)
			return Vec3.ZERO;
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_TP = Vec3.ZERO;
		double sca_K = 0;
		double sca_scale = 0;
		sca_K = entity.getPersistentData().getDouble("K_Player");
		vec_TP = new Vec3((x - CacModVariables.MapVariables.get(world).Pos_player_x), 0, (z - CacModVariables.MapVariables.get(world).Pos_player_z));
		sca_scale = (-(sca_K / vec_TP.lengthSqr())) - 4;
		if (sca_scale > 0) {
			sca_scale = 0;
		}
		vec_field = (vec_TP.normalize()).scale(sca_scale);
		return vec_field;
	}
}
