package net.owo.cac;

import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.owo.cac.CstState;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderHandler {
	
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		boolean ismeowview;
		ismeowview = CstState.getMeowView();
    	
        // Check if this is the hotbar overlay
        if (ismeowview) {
        	if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) event.setCanceled(true);
        }
        
		GuiGraphics gg = event.getGuiGraphics();
		gg.blit(new ResourceLocation("cac:textures/screens/texture_bar_frame.png"), 40, 40, 0, 0, 96, 8, 96, 8);
		gg.blit(new ResourceLocation("cac:textures/screens/texture_bar_gauge.png"), 40, 40, 0, 0, 64, 8, 96, 8);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CstState.getMeowView()) event.setCanceled(true);
    }
    
}