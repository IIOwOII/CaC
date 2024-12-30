
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.CacMod;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CacMod.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(CacModBlocks.BLK_OBSTACLE.get().asItem());
			tabData.accept(CacModBlocks.BLK_WALL.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			tabData.accept(CacModItems.CAC_TEST_ITEM.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(CacModItems.ENT_CAT_SPAWN_EGG.get());
			tabData.accept(CacModItems.ENT_PLAYER_CAT_SPAWN_EGG.get());
			tabData.accept(CacModItems.ENT_MOUSE_SPAWN_EGG.get());
			tabData.accept(CacModItems.ENT_PLAYER_MOUSE_SPAWN_EGG.get());
		}
	}
}
