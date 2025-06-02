
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.item.CacTestItemItem;
import net.owo.cac.item.CacLogoItem;
import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

public class CacModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, CacMod.MODID);
	public static final RegistryObject<Item> ENT_CAT_SPAWN_EGG = REGISTRY.register("ent_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_CAT, -16777216, -65536, new Item.Properties()));
	public static final RegistryObject<Item> CAC_TEST_ITEM = REGISTRY.register("cac_test_item", () -> new CacTestItemItem());
	public static final RegistryObject<Item> ENT_PLAYER_CAT_SPAWN_EGG = REGISTRY.register("ent_player_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PLAYER_CAT, -1, -65536, new Item.Properties()));
	public static final RegistryObject<Item> BLK_OBSTACLE = block(CacModBlocks.BLK_OBSTACLE);
	public static final RegistryObject<Item> BLK_WALL = block(CacModBlocks.BLK_WALL);
	public static final RegistryObject<Item> ENT_MOUSE_SPAWN_EGG = REGISTRY.register("ent_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_MOUSE, -16777216, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PLAYER_MOUSE_SPAWN_EGG = REGISTRY.register("ent_player_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PLAYER_MOUSE, -1, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> ENT_MEOWCAM_SPAWN_EGG = REGISTRY.register("ent_meowcam_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_MEOWCAM, -13210, -10066330, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PSEUDO_CAT_SPAWN_EGG = REGISTRY.register("ent_pseudo_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PSEUDO_CAT, -6710887, -65536, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PSEUDO_MOUSE_SPAWN_EGG = REGISTRY.register("ent_pseudo_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PSEUDO_MOUSE, -6710887, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> BLK_FENCE = block(CacModBlocks.BLK_FENCE);
	public static final RegistryObject<Item> CAC_LOGO = REGISTRY.register("cac_logo", () -> new CacLogoItem());

	// Start of user code block custom items
	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
