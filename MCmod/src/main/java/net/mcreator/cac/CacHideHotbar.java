package net.mcreator.cac;

import net.minecraft.world.level.LevelAccessor;

import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.mcreator.cac.network.CacModVariables;

@Mod.EventBusSubscriber(modid = "cac", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CacHideHotbar {
    @SubscribeEvent
    public static void hideHotbar(RenderGuiOverlayEvent.Pre event, LevelAccessor world) {
    	if (CacModVariables.MapVariables.get(world).Option_hotbar) {
			return;
		} else {
	        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
	            event.setCanceled(true);
	        }
		}
    }
}