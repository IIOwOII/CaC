package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import net.owo.cac.CstState;
import net.owo.cac.CstRenderComponent;
import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderHandler {
	
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
    	// Check if this is the hotbar overlay
		boolean ismeowview = CstState.getMeowView();
        if (ismeowview) {
        	if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) event.setCanceled(true);
        }
        
        int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		GuiGraphics gg = event.getGuiGraphics();
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
    	GuiGraphics gg = event.getGuiGraphics();
    	if (CacModVariables.Switch_blank) {
    		CstRenderComponent.renderBlank(gg, 427, 240);
    	}
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CstState.getMeowView()) event.setCanceled(true);
    }
    
}