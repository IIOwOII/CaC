package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderComponent {
	public static final int GW = 427;
	public static final int GH = 240;
	
	public static ResourceLocation bar_frame = new ResourceLocation("cac:textures/screens/texture_bar_frame.png");
	public static ResourceLocation bar_gauge = new ResourceLocation("cac:textures/screens/texture_bar_gauge.png");
	public static ResourceLocation patch_white = new ResourceLocation("cac:textures/screens/texture_patch_white.png");
	public static ResourceLocation patch_black = new ResourceLocation("cac:textures/screens/texture_patch_black.png");
	
	public static ResourceLocation gui_blank = new ResourceLocation("cac:textures/screens/gui_blank.png");
	public static ResourceLocation blank = new ResourceLocation("cac:textures/screens/texture_blank.png");
	public static ResourceLocation slide_frame = new ResourceLocation("cac:textures/screens/texture_slide.png");
	public static ResourceLocation slide_trace = new ResourceLocation("cac:textures/screens/texture_slide_trace.png");
	public static ResourceLocation slide_cursor = new ResourceLocation("cac:textures/screens/texture_slide_cursor.png");

	
	public static void renderBar(GuiGraphics gg, double value, double value_max) {
		int gauge = (int)(96*(value/value_max));
		gg.blit(bar_frame, GW/2-48, 20, 0, 0, 96, 8, 96, 8);
		gg.blit(bar_gauge, GW/2-48, 20, 0, 0, gauge, 8, 96, 8);
	}

	public static void renderPatchWhite(GuiGraphics gg) {
		gg.blit(patch_white, GW-64, 0, 0, 0, 64, 64, 64, 64);
	}

	public static void renderPatchBlack(GuiGraphics gg) {
		gg.blit(patch_black, GW-64, 0, 0, 0, 64, 64, 64, 64);
	}

	public static void renderGuiBlank(GuiGraphics gg) {
		gg.blit(gui_blank, 0, 0, 0, 0, GW, GH, GW, GH);
	}

	public static void renderBlank(GuiGraphics gg) {
		gg.blit(blank, 0, 0, 0, 0, GW, GH, GW, GH);
	}

	public static void renderSlide(GuiGraphics gg, double value, double value_old, double value_max) {
		double ratio = value/value_max;
		double ratio_old = value_old/value_max;
		gg.blit(slide_frame, GW/2-200, GH/2+54, 0, 0, 400, 32, 400, 32);
		gg.blit(slide_trace, GW/2-194+(int)(384*ratio_old), GH/2+60, 0, 0, 4, 20, 4, 20);
		gg.blit(slide_cursor, GW/2-194+(int)(384*ratio), GH/2+60, 0, 0, 4, 20, 4, 20);
	}
}
