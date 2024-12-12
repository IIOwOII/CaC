package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;

public class FncDistancePlayerProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double z) {
		Vec3 vec_player = Vec3.ZERO;
		vec_player = new Vec3(CacModVariables.MapVariables.get(world).Pos_player_x, 0, CacModVariables.MapVariables.get(world).Pos_player_z);
		return (new Vec3(x, 0, z)).subtract(vec_player);
	}
}
