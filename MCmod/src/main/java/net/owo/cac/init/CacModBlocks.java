
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.block.BlkWallBlock;
import net.owo.cac.block.BlkValuestoneBlock;
import net.owo.cac.block.BlkValuepatchBlock;
import net.owo.cac.block.BlkTapeBlockBlock;
import net.owo.cac.block.BlkSwitchstoneBlock;
import net.owo.cac.block.BlkParasolBlock;
import net.owo.cac.block.BlkObstacleBlock;
import net.owo.cac.block.BlkMissionBlock;
import net.owo.cac.block.BlkHurdleBlock;
import net.owo.cac.block.BlkFloweringAzaleaGrassBlock;
import net.owo.cac.block.BlkFlagBlock;
import net.owo.cac.block.BlkFenceBlock;
import net.owo.cac.block.BlkCheckpointBlock;
import net.owo.cac.block.BlkAzaleaGrassBlock;
import net.owo.cac.block.BlkArrowBlock;
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
	public static final RegistryObject<Block> BLK_ARROW = REGISTRY.register("blk_arrow", () -> new BlkArrowBlock());
	public static final RegistryObject<Block> BLK_VALUESTONE = REGISTRY.register("blk_valuestone", () -> new BlkValuestoneBlock());
	public static final RegistryObject<Block> BLK_TAPE_BLOCK = REGISTRY.register("blk_tape_block", () -> new BlkTapeBlockBlock());
	public static final RegistryObject<Block> BLK_VALUEPATCH = REGISTRY.register("blk_valuepatch", () -> new BlkValuepatchBlock());
	public static final RegistryObject<Block> BLK_MISSION = REGISTRY.register("blk_mission", () -> new BlkMissionBlock());
	public static final RegistryObject<Block> BLK_FLAG = REGISTRY.register("blk_flag", () -> new BlkFlagBlock());
	public static final RegistryObject<Block> BLK_PARASOL = REGISTRY.register("blk_parasol", () -> new BlkParasolBlock());
	public static final RegistryObject<Block> BLK_FLOWERING_AZALEA_GRASS = REGISTRY.register("blk_flowering_azalea_grass", () -> new BlkFloweringAzaleaGrassBlock());
	public static final RegistryObject<Block> BLK_AZALEA_GRASS = REGISTRY.register("blk_azalea_grass", () -> new BlkAzaleaGrassBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
