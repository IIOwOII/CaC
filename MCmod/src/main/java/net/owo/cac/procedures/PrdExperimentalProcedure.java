package net.owo.cac.procedures;

import net.owo.cac.init.CacModBlocks;
import net.owo.cac.CacMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == CacModBlocks.BLK_PARASOL.get()) {
			CacMod.LOGGER.info(Vec3.atLowerCornerOf(BlockPos.containing((new Vec3(x, y, z)))));
		}
	}
}
