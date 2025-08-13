
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.block.BlkWallBlock;
import net.owo.cac.block.BlkSwitchstoneBlock;
import net.owo.cac.block.BlkObstacleBlock;
import net.owo.cac.block.BlkHurdleBlock;
import net.owo.cac.block.BlkFenceBlock;
import net.owo.cac.block.BlkCheckpointBlock;
import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class CacModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CacMod.MODID);
	public static final RegistryObject<Block> BLK_OBSTACLE = REGISTRY.register("blk_obstacle", () -> new BlkObstacleBlock());
	public static final RegistryObject<Block> BLK_WALL = REGISTRY.register("blk_wall", () -> new BlkWallBlock());
	public static final RegistryObject<Block> BLK_FENCE = REGISTRY.register("blk_fence", () -> new BlkFenceBlock());
	public static final RegistryObject<Block> BLK_CHECKPOINT = REGISTRY.register("blk_checkpoint", () -> new BlkCheckpointBlock());
	public static final RegistryObject<Block> BLK_HURDLE = REGISTRY.register("blk_hurdle", () -> new BlkHurdleBlock());
	public static final RegistryObject<Block> BLK_SWITCHSTONE = REGISTRY.register("blk_switchstone", () -> new BlkSwitchstoneBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
