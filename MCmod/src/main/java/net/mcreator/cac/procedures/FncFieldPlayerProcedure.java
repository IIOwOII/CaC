package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

public class FncFieldPlayerProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double z) {
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_player = Vec3.ZERO;
		vec_field = new Vec3(0, 0, 0);
		vec_player = FncDistancePlayerProcedure.execute(world, x, z).scale(CacModVariables.MapVariables.get(world).Pmt_distance_scale);
		vec_field = vec_field.add(((vec_player.normalize()).scale((1 / vec_player.lengthSqr()))));
		return vec_field;
	}
}
