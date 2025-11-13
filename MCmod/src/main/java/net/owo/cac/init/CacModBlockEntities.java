
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.block.entity.BlkValuestoneBlockEntity;
import net.owo.cac.block.entity.BlkValuepatchBlockEntity;
import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

public class CacModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CacMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> BLK_VALUESTONE = register("blk_valuestone", CacModBlocks.BLK_VALUESTONE, BlkValuestoneBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLK_VALUEPATCH = register("blk_valuepatch", CacModBlocks.BLK_VALUEPATCH, BlkValuepatchBlockEntity::new);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
