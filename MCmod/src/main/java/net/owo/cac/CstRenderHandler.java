package net.owo.cac;

import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderHandler {
	
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		boolean ismeowview;
		ismeowview = CstState.getMeowview();
    	
        // Check if this is the hotbar overlay
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            if (ismeowview)
                event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            if (ismeowview)
                event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            if (ismeowview)
                event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) {
            if (ismeowview)
                event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
        	if (ismeowview)
        		event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CstState.getMeowview())
            event.setCanceled(true);
    }
}