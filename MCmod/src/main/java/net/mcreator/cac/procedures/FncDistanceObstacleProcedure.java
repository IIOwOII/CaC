package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.init.CacModBlocks;

import java.util.ArrayList;

public class FncDistanceObstacleProcedure {
	public static ArrayList<Vec3> execute(LevelAccessor world, double x, double y, double z) {
		ArrayList<Vec3> list_obstacle;
		double sx = 0;
		double sz = 0;
		list_obstacle = new ArrayList<Vec3>();
		sx = CacModVariables.MapVariables.get(world).Pmt_far * (-1);
		for (int index0 = 0; index0 < (int) (CacModVariables.MapVariables.get(world).Pmt_far * 2 + 1); index0++) {
			sz = CacModVariables.MapVariables.get(world).Pmt_far * (-1);
			for (int index1 = 0; index1 < (int) (CacModVariables.MapVariables.get(world).Pmt_far * 2 + 1); index1++) {
				if ((world.getBlockState(BlockPos.containing(x + sx, y, z + sz))).getBlock() == CacModBlocks.BLK_OBSTACLE.get()) {
					list_obstacle.add((new Vec3((x - (Math.floor(x + sx) + 0.5)), 0, (z - (Math.floor(z + sz) + 0.5)))));
				}
				sz = sz + 1;
			}
			sx = sx + 1;
		}
		return list_obstacle;
	}
}
