package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

import java.util.ArrayList;

public class FncFieldWallProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double y, double z) {
		ArrayList<Vec3> list_wall;
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_wall = Vec3.ZERO;
		vec_field = new Vec3(0, 0, 0);
		list_wall = FncDistanceWallProcedure.execute(world, x, y, z);
		for (Vec3 vectoriterator : list_wall) {
			vec_wall = vectoriterator.scale(CacModVariables.MapVariables.get(world).Pmt_distance_scale);
			vec_field = vec_field.add(((vec_wall.normalize()).scale((1 / vec_wall.lengthSqr()))));
		}
		return vec_field;
	}
}
