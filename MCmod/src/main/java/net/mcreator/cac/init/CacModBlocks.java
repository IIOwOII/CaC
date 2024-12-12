
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.cac.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.mcreator.cac.block.BlkWallBlock;
import net.mcreator.cac.block.BlkObstacleBlock;
import net.mcreator.cac.CacMod;

public class CacModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CacMod.MODID);
	public static final RegistryObject<Block> BLK_OBSTACLE = REGISTRY.register("blk_obstacle", () -> new BlkObstacleBlock());
	public static final RegistryObject<Block> BLK_WALL = REGISTRY.register("blk_wall", () -> new BlkWallBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
