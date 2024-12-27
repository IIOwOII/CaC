package net.owo.cac;

import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderHandler {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        // Check if this is the hotbar overlay
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            // If we do NOT want the hotbar to appear, cancel the event
            if (!CstState.hotbarVisible) {
                event.setCanceled(true);
            }
        }
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            if (!CstState.hotbarVisible) {
                event.setCanceled(true);
            }
        }
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            if (!CstState.hotbarVisible) {
                event.setCanceled(true);
            }
        }
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) {
            if (!CstState.hotbarVisible) {
                event.setCanceled(true);
            }
        }
    }
}