package net.mcreator.cac.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.init.CacModBlocks;

public class FncScanObstacleProcedure {
	public static ListTag execute(LevelAccessor world) {
		double offset_x = 0;
		double offset_z = 0;
		double offset_y = 0;
		double sx = 0;
		double sz = 0;
		double radius_map = 0;
		BlockState block_curr = Blocks.AIR.defaultBlockState();
		BlockState block_next = Blocks.AIR.defaultBlockState();
		BlockState block_prev = Blocks.AIR.defaultBlockState();
		boolean ispoint_start = false;
		boolean ispoint_end = false;
		ListTag list_vertice_temp;
		ListTag list_line_temp;
		ListTag list_line;
		offset_x = CacModVariables.MapVariables.get(world).Pos_offset.x();
		offset_y = CacModVariables.MapVariables.get(world).Pos_offset.y();
		offset_z = CacModVariables.MapVariables.get(world).Pos_offset.z();
		radius_map = CacModVariables.MapVariables.get(world).Radius_map;
		list_line = new ListTag();
		list_vertice_temp = new ListTag();
		list_line_temp = new ListTag();
		sx = -radius_map;
		for (int index0 = 0; index0 < (int) (radius_map * 2 + 1); index0++) {
			sz = -radius_map;
			block_prev = (world.getBlockState(BlockPos.containing(sx + offset_x, offset_y, sz - 1 + offset_z)));
			block_curr = (world.getBlockState(BlockPos.containing(sx + offset_x, offset_y, sz + offset_z)));
			for (int index1 = 0; index1 < (int) (radius_map * 2 + 1); index1++) {
				ispoint_start = false;
				ispoint_end = false;
				block_next = (world.getBlockState(BlockPos.containing(sx + offset_x, offset_y, sz + 1 + offset_z)));
				if (block_curr.getBlock() == CacModBlocks.BLK_OBSTACLE.get()) {
					if (!(block_prev.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) {
						ispoint_start = true;
					}
					if (!(block_next.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) {
						ispoint_end = true;
					}
				}
				if (ispoint_start || ispoint_end) {
					list_vertice_temp = new ListTag();
					list_vertice_temp.addTag(0, IntTag.valueOf((int) sx));
					list_vertice_temp.addTag(1, IntTag.valueOf((int) sz));
					if (ispoint_start && !ispoint_end) {
						list_line_temp = new ListTag();
						list_line_temp.addTag(0, (list_vertice_temp.copy()));
					} else if (!ispoint_start && ispoint_end) {
						list_line_temp.addTag(1, (list_vertice_temp.copy()));
						list_line.addTag(list_line.size(), (list_line_temp.copy()));
					}
				}
				sz = sz + 1;
				block_prev = block_curr;
				block_curr = block_next;
			}
			sx = sx + 1;
		}
		sz = -radius_map;
		for (int index2 = 0; index2 < (int) (radius_map * 2 + 1); index2++) {
			sx = -radius_map;
			block_prev = (world.getBlockState(BlockPos.containing(sx - 1 + offset_x, offset_y, sz + offset_z)));
			block_curr = (world.getBlockState(BlockPos.containing(sx + offset_x, offset_y, sz + offset_z)));
			for (int index3 = 0; index3 < (int) (radius_map * 2 + 1); index3++) {
				ispoint_start = false;
				ispoint_end = false;
				block_next = (world.getBlockState(BlockPos.containing(sx + 1 + offset_x, offset_y, sz + offset_z)));
				if (block_curr.getBlock() == CacModBlocks.BLK_OBSTACLE.get()) {
					if (!(block_prev.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) {
						ispoint_start = true;
					}
					if (!(block_next.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) {
						ispoint_end = true;
					}
				}
				if (ispoint_start || ispoint_end) {
					list_vertice_temp = new ListTag();
					list_vertice_temp.addTag(0, IntTag.valueOf((int) sx));
					list_vertice_temp.addTag(1, IntTag.valueOf((int) sz));
					if (ispoint_start && !ispoint_end) {
						list_line_temp = new ListTag();
						list_line_temp.addTag(0, (list_vertice_temp.copy()));
					} else if (!ispoint_start && ispoint_end) {
						list_line_temp.addTag(1, (list_vertice_temp.copy()));
						list_line.addTag(list_line.size(), (list_line_temp.copy()));
					}
				}
				sx = sx + 1;
				block_prev = block_curr;
				block_curr = block_next;
			}
			sz = sz + 1;
		}
		return list_line;
	}
}
