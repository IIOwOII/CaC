package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.entity.EntMouseEntity;
import net.mcreator.cac.entity.EntCatEntity;

import java.util.Comparator;

public class FncDistanceProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double y, double z) {
		Vec3 pos_self = Vec3.ZERO;
		Vec3 pos_opponent = Vec3.ZERO;
		if (!world.getEntitiesOfClass(EntMouseEntity.class, AABB.ofSize(new Vec3(x, y, z), CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far), e -> true)
				.isEmpty()) {
			pos_self = new Vec3(x, 0, z);
			pos_opponent = (((Entity) world.getEntitiesOfClass(EntMouseEntity.class,
					AABB.ofSize(new Vec3(x, y, z), CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far), e -> true).stream().sorted(new Object() {
						Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
							return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
						}
					}.compareDistOf(x, y, z)).findFirst().orElse(null)).position()).multiply(1, 0, 1);
		} else if (!world
				.getEntitiesOfClass(EntCatEntity.class, AABB.ofSize(new Vec3(x, y, z), CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far), e -> true)
				.isEmpty()) {
			pos_self = new Vec3(x, 0, z);
			pos_opponent = (((Entity) world
					.getEntitiesOfClass(EntCatEntity.class, AABB.ofSize(new Vec3(x, y, z), CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far, CacModVariables.MapVariables.get(world).Pmt_far), e -> true)
					.stream().sorted(new Object() {
						Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
							return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
						}
					}.compareDistOf(x, y, z)).findFirst().orElse(null)).position()).multiply(1, 0, 1);
		}
		return pos_opponent.subtract(pos_self);
	}
}
