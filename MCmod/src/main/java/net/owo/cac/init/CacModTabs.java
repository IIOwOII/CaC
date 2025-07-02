
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.owo.cac.init;

import net.owo.cac.CacMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CacModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CacMod.MODID);
	public static final RegistryObject<CreativeModeTab> CAC_TAB = REGISTRY.register("cac_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.cac.cac_tab")).icon(() -> new ItemStack(CacModItems.CAC_LOGO.get())).displayItems((parameters, tabData) -> {
				tabData.accept(CacModItems.CAC_TEST_ITEM.get());
				tabData.accept(CacModBlocks.BLK_OBSTACLE.get().asItem());
				tabData.accept(CacModBlocks.BLK_WALL.get().asItem());
				tabData.accept(CacModItems.ENT_MOUSE_SPAWN_EGG.get());
				tabData.accept(CacModItems.ENT_PLAYER_MOUSE_SPAWN_EGG.get());
				tabData.accept(CacModItems.ENT_PSEUDO_MOUSE_SPAWN_EGG.get());
				tabData.accept(CacModBlocks.BLK_FENCE.get().asItem());
				tabData.accept(CacModItems.CAC_LOGO.get());
				tabData.accept(CacModItems.ENT_CAT_SPAWN_EGG.get());
				tabData.accept(CacModItems.ENT_PLAYER_CAT_SPAWN_EGG.get());
				tabData.accept(CacModItems.ENT_PSEUDO_CAT_SPAWN_EGG.get());
				tabData.accept(CacModBlocks.BLK_CHECKPOINT.get().asItem());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(CacModItems.ENT_MEOWCAM_SPAWN_EGG.get());
		}
	}
}
