package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModBlocks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;

public class FncScanWallProcedure {
	public static ListTag execute(LevelAccessor world) {
		BlockState block_curr = Blocks.AIR.defaultBlockState();
		BlockState block_next = Blocks.AIR.defaultBlockState();
		BlockState block_prev = Blocks.AIR.defaultBlockState();
		boolean ispoint_start = false;
		boolean ispoint_end = false;
		ListTag list_vertice_temp;
		ListTag list_line_temp;
		ListTag list_line;
		double offset_y = 0;
		double sx = 0;
		double sz = 0;
		double bx_offset = 0;
		double bz_offset = 0;
		Vec3 vec_SE = Vec3.ZERO;
		bx_offset = Math.round(CacModVariables.Pos_offset.x() - 0.5);
		bz_offset = Math.round(CacModVariables.Pos_offset.z() - 0.5);
		offset_y = CacModVariables.Pos_offset.y();
		vec_SE = CacModVariables.Pos_border_end.subtract(CacModVariables.Pos_border_start);
		list_line = new ListTag();
		list_vertice_temp = new ListTag();
		list_line_temp = new ListTag();
		sx = Math.round(CacModVariables.Pos_border_start.x() - 0.5);
		for (int index0 = 0; index0 < (int) (vec_SE.x() + 1); index0++) {
			sz = Math.round(CacModVariables.Pos_border_start.z() - 0.5);
			block_prev = (world.getBlockState(BlockPos.containing(sx, offset_y, sz - 1)));
			block_curr = (world.getBlockState(BlockPos.containing(sx, offset_y, sz)));
			for (int index1 = 0; index1 < (int) (vec_SE.z() + 1); index1++) {
				ispoint_start = false;
				ispoint_end = false;
				block_next = (world.getBlockState(BlockPos.containing(sx, offset_y, sz + 1)));
				if (block_curr.getBlock() == CacModBlocks.BLK_WALL.get()) {
					if (!(block_prev.getBlock() == CacModBlocks.BLK_WALL.get())) {
						ispoint_start = true;
					}
					if (!(block_next.getBlock() == CacModBlocks.BLK_WALL.get())) {
						ispoint_end = true;
					}
				}
				if (ispoint_start || ispoint_end) {
					list_vertice_temp = new ListTag();
					list_vertice_temp.addTag(0, IntTag.valueOf((int) Math.round(sx - bx_offset)));
					list_vertice_temp.addTag(1, IntTag.valueOf((int) Math.round(sz - bz_offset)));
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
		sz = Math.round(CacModVariables.Pos_border_start.z() - 0.5);
		for (int index2 = 0; index2 < (int) (vec_SE.z() + 1); index2++) {
			sx = Math.round(CacModVariables.Pos_border_start.x() - 0.5);
			block_prev = (world.getBlockState(BlockPos.containing(sx - 1, offset_y, sz)));
			block_curr = (world.getBlockState(BlockPos.containing(sx, offset_y, sz)));
			for (int index3 = 0; index3 < (int) (vec_SE.x() + 1); index3++) {
				ispoint_start = false;
				ispoint_end = false;
				block_next = (world.getBlockState(BlockPos.containing(sx + 1, offset_y, sz)));
				if (block_curr.getBlock() == CacModBlocks.BLK_WALL.get()) {
					if (!(block_prev.getBlock() == CacModBlocks.BLK_WALL.get())) {
						ispoint_start = true;
					}
					if (!(block_next.getBlock() == CacModBlocks.BLK_WALL.get())) {
						ispoint_end = true;
					}
				}
				if (ispoint_start || ispoint_end) {
					list_vertice_temp = new ListTag();
					list_vertice_temp.addTag(0, IntTag.valueOf((int) Math.round(sx - bx_offset)));
					list_vertice_temp.addTag(1, IntTag.valueOf((int) Math.round(sz - bz_offset)));
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
