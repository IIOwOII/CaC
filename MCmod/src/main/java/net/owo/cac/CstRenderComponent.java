package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderComponent {
	public static ResourceLocation bar_frame = new ResourceLocation("cac:textures/screens/texture_bar_frame.png");
	public static ResourceLocation bar_gauge = new ResourceLocation("cac:textures/screens/texture_bar_gauge.png");
	public static ResourceLocation patch_white = new ResourceLocation("cac:textures/screens/texture_patch_white.png");
	public static ResourceLocation patch_black = new ResourceLocation("cac:textures/screens/texture_patch_black.png");
	
	public static ResourceLocation gui_blank = new ResourceLocation("cac:textures/screens/gui_blank.png");
	public static ResourceLocation blank = new ResourceLocation("cac:textures/screens/texture_blank.png");
	public static ResourceLocation slide_frame = new ResourceLocation("cac:textures/screens/texture_slide.png");
	public static ResourceLocation slide_trace = new ResourceLocation("cac:textures/screens/texture_slide_trace.png");
	public static ResourceLocation slide_cursor = new ResourceLocation("cac:textures/screens/texture_slide_cursor.png");

	public static ResourceLocation button_select = new ResourceLocation("cac:textures/screens/button_select.png");
	public static ResourceLocation button_yes = new ResourceLocation("cac:textures/screens/button_yes.png");
	public static ResourceLocation button_no = new ResourceLocation("cac:textures/screens/button_no.png");

	// Bar
	public static void renderBar(GuiGraphics gg, int gw, int gh, double value, double value_max) {
		int gauge = (int)(96*(value/value_max));
		gg.blit(bar_frame, gw/2-48, 20, 0, 0, 96, 8, 96, 8);
		gg.blit(bar_gauge, gw/2-48, 20, 0, 0, gauge, 8, 96, 8);
	}

	// Patch (sEEG)
	public static void renderPatchWhite(GuiGraphics gg, int gw, int gh) {
		gg.blit(patch_white, gw-64, 0, 0, 0, 64, 64, 64, 64);
	}

	public static void renderPatchBlack(GuiGraphics gg, int gw, int gh) {
		gg.blit(patch_black, gw-64, 0, 0, 0, 64, 64, 64, 64);
	}

	// Blank
	public static void renderGuiBlank(GuiGraphics gg, int gw, int gh) {
		gg.blit(gui_blank, 0, 0, 0, 0, gw, gh, gw, gh);
	}

	public static void renderBlank(GuiGraphics gg, int gw, int gh) {
		gg.blit(blank, 0, 0, 0, 0, gw, gh, gw, gh);
	}

	// Slide
	public static void renderSlide(GuiGraphics gg, int gw, int gh, double value, double value_old, double value_max) {
		double ratio = value/value_max;
		double ratio_old = value_old/value_max;
		gg.blit(slide_frame, gw/2-200, gh/2+54, 0, 0, 400, 32, 400, 32);
		gg.blit(slide_trace, gw/2-194+(int)(384*ratio_old), gh/2+60, 0, 0, 4, 20, 4, 20);
		gg.blit(slide_cursor, gw/2-194+(int)(384*ratio), gh/2+60, 0, 0, 4, 20, 4, 20);
	}

	// Button
	public static void renderButtonSelect(GuiGraphics gg, int gw, int gh, int type) {
		int ox = 0;
		int oy = gh/2+32;
		switch (type) {
			case 0: ox = (gw/4)-40; break;
			case 1: ox = (gw/4)*3-40; break;
			case -1: ox = (gw/2)-40; break;
		}
		gg.blit(button_select, ox, oy, 0, 0, 80, 32, 80, 32);
	}
	
	public static void renderButtonYes(GuiGraphics gg, int gw, int gh, int type) {
		int ox = 0;
		int oy = gh/2+32;
		switch (type) {
			case 0: ox = (gw/4)-40; break;
			case 1: ox = (gw/4)*3-40; break;
		}
		gg.blit(button_yes, ox, oy, 0, 0, 80, 32, 80, 32);
	}

	public static void renderButtonNo(GuiGraphics gg, int gw, int gh, int type) {
		int ox = 0;
		int oy = gh/2+32;
		switch (type) {
			case 0: ox = (gw/4)-40; break;
			case 1: ox = (gw/4)*3-40; break;
		}
		gg.blit(button_no, ox, oy, 0, 0, 80, 32, 80, 32);
	}
}
