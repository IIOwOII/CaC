
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.item.CacTestItemItem;
import net.owo.cac.item.CacLogoItem;
import net.owo.cac.item.CacBuilderToolItem;
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
	public static final RegistryObject<Item> CAC_TEST_ITEM = REGISTRY.register("cac_test_item", () -> new CacTestItemItem());
	public static final RegistryObject<Item> BLK_OBSTACLE = block(CacModBlocks.BLK_OBSTACLE);
	public static final RegistryObject<Item> BLK_WALL = block(CacModBlocks.BLK_WALL);
	public static final RegistryObject<Item> ENT_MOUSE_SPAWN_EGG = REGISTRY.register("ent_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_MOUSE, -16777216, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PLAYER_MOUSE_SPAWN_EGG = REGISTRY.register("ent_player_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PLAYER_MOUSE, -1, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> ENT_MEOWCAM_SPAWN_EGG = REGISTRY.register("ent_meowcam_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_MEOWCAM, -13210, -10066330, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PSEUDO_MOUSE_SPAWN_EGG = REGISTRY.register("ent_pseudo_mouse_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PSEUDO_MOUSE, -6710887, -16711936, new Item.Properties()));
	public static final RegistryObject<Item> BLK_FENCE = block(CacModBlocks.BLK_FENCE);
	public static final RegistryObject<Item> CAC_LOGO = REGISTRY.register("cac_logo", () -> new CacLogoItem());
	public static final RegistryObject<Item> ENT_CAT_SPAWN_EGG = REGISTRY.register("ent_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_CAT, -16777216, -65536, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PLAYER_CAT_SPAWN_EGG = REGISTRY.register("ent_player_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PLAYER_CAT, -1, -65536, new Item.Properties()));
	public static final RegistryObject<Item> ENT_PSEUDO_CAT_SPAWN_EGG = REGISTRY.register("ent_pseudo_cat_spawn_egg", () -> new ForgeSpawnEggItem(CacModEntities.ENT_PSEUDO_CAT, -6710887, -65536, new Item.Properties()));
	public static final RegistryObject<Item> BLK_CHECKPOINT = block(CacModBlocks.BLK_CHECKPOINT);
	public static final RegistryObject<Item> BLK_HURDLE = block(CacModBlocks.BLK_HURDLE);
	public static final RegistryObject<Item> BLK_SWITCHSTONE = block(CacModBlocks.BLK_SWITCHSTONE);
	public static final RegistryObject<Item> CAC_BUILDER_TOOL = REGISTRY.register("cac_builder_tool", () -> new CacBuilderToolItem());
	public static final RegistryObject<Item> BLK_ARROW = block(CacModBlocks.BLK_ARROW);
	public static final RegistryObject<Item> BLK_VALUESTONE = block(CacModBlocks.BLK_VALUESTONE);
	public static final RegistryObject<Item> BLK_TAPE_BLOCK = block(CacModBlocks.BLK_TAPE_BLOCK);
	public static final RegistryObject<Item> BLK_VALUEPATCH = block(CacModBlocks.BLK_VALUEPATCH);
	public static final RegistryObject<Item> BLK_MISSION = block(CacModBlocks.BLK_MISSION);
	public static final RegistryObject<Item> BLK_FLAG = block(CacModBlocks.BLK_FLAG);
	public static final RegistryObject<Item> BLK_PARASOL = block(CacModBlocks.BLK_PARASOL);
	public static final RegistryObject<Item> BLK_FLOWERING_AZALEA_GRASS = block(CacModBlocks.BLK_FLOWERING_AZALEA_GRASS);
	public static final RegistryObject<Item> BLK_AZALEA_GRASS = block(CacModBlocks.BLK_AZALEA_GRASS);

	// Start of user code block custom items
	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
