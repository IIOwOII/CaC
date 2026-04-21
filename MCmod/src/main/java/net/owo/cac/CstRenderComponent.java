package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderComponent {
	public static ResourceLocation bar_frame = new ResourceLocation("cac:textures/screens/texture_bar_frame.png");
	public static ResourceLocation bar_gauge = new ResourceLocation("cac:textures/screens/texture_bar_gauge.png");
	public static ResourceLocation patch_white = new ResourceLocation("cac:textures/screens/texture_patch_white.png");
	public static ResourceLocation patch_black = new ResourceLocation("cac:textures/screens/texture_patch_black.png");

	public static ResourceLocation blank_lightgrey = new ResourceLocation("cac:textures/screens/texture_lightgrey.png");
	public static ResourceLocation blank = new ResourceLocation("cac:textures/screens/texture_blank.png");
	public static ResourceLocation gui_blank = new ResourceLocation("cac:textures/screens/gui_blank.png");
	public static ResourceLocation slide_frame = new ResourceLocation("cac:textures/screens/texture_slide.png");
	public static ResourceLocation slide_trace = new ResourceLocation("cac:textures/screens/texture_slide_trace.png");
	public static ResourceLocation slide_cursor = new ResourceLocation("cac:textures/screens/texture_slide_cursor.png");
	public static ResourceLocation slide_text_right = new ResourceLocation("cac:textures/screens/text_slide_right.png");
	public static ResourceLocation slide_text_left = new ResourceLocation("cac:textures/screens/text_slide_left.png");

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
		gg.blit(patch_white, gw-32, 0, 0, 0, 32, 32, 32, 32);
	}

	public static void renderPatchBlack(GuiGraphics gg, int gw, int gh) {
		gg.blit(patch_black, gw-32, 0, 0, 0, 32, 32, 32, 32);
	}

	// Blank
	public static void renderGuiBlank(GuiGraphics gg, int gw, int gh) {
		gg.blit(gui_blank, 0, 0, 0, 0, gw, gh, gw, gh);
	}

	public static void renderBlank(GuiGraphics gg, int gw, int gh) {
		gg.blit(blank, 0, 0, 0, 0, gw, gh, gw, gh);
	}

	public static void renderBlankLightgrey(GuiGraphics gg, int gw, int gh) {
		gg.blit(blank_lightgrey, 0, 0, 0, 0, gw, gh, gw, gh);
	}

	// Slide
	public static void renderSlide(GuiGraphics gg, int gw, int gh, double value, double value_old, double value_max) {
		double ratio = value/value_max;
		double ratio_old = value_old/value_max;
		gg.blit(slide_frame, gw/2-200, gh/2+54, 0, 0, 400, 32, 400, 32);
		gg.blit(slide_trace, gw/2-194+(int)(384*ratio_old), gh/2+60, 0, 0, 4, 20, 4, 20);
		gg.blit(slide_cursor, gw/2-194+(int)(384*ratio), gh/2+60, 0, 0, 4, 20, 4, 20);
	}

	// Slide Text
	public static void renderSlideText(GuiGraphics gg, int gw, int gh) {
		gg.blit(slide_text_left, gw/2-200, gh/2+86, 0, 0, 100, 30, 100, 30);
		gg.blit(slide_text_right, gw/2+100, gh/2+86, 0, 0, 100, 30, 100, 30);
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

	// Color define
	public static float[] getColor(char color) {
        float[] c_map = {0F, 0F, 0F};
		if (color == 'k') {
		} else if (color == 'r') {
			c_map[0] = 1F;
		} else if (color == 'g') {
			c_map[1] = 1F;
		} else if (color == 'b') {
			c_map[2] = 1F;
		} else if (color == 'x') {
			// RED_A: #F7A1A3
			c_map[0] = 0.9686F;
			c_map[1] = 0.6314F;
			c_map[2] = 0.6392F;
		} else if (color == 'y') {
			// RED_C: #FC6255
			c_map[0] = 0.9882F;
			c_map[1] = 0.3843F;
			c_map[2] = 0.3333F;
		} else if (color == 'z') {
			// YELLOW_B: #FFEA94
			c_map[0] = 1F;
			c_map[1] = 0.9176F;
			c_map[2] = 0.5804F;
		} else if (color == 'w') {
			// GRAY_B: #BBBBBB
			c_map[0] = 0.7333F;
			c_map[1] = 0.7333F;
			c_map[2] = 0.7333F;
		}
		return c_map;
	}
	
	// Draw line
	public static void renderLine(PoseStack ps, VertexConsumer vc, Vec3 vec_start, Vec3 vec_end, char color) {
		PoseStack.Pose pose = ps.last();
		Vec3 vec_N = Vec3.ZERO;
		vec_N = vec_end.subtract(vec_start).normalize();
		float[] c_map = getColor(color);
		
		// start
		vc.vertex(pose.pose(), (float)vec_start.x(), (float)vec_start.y(), (float)vec_start.z());
		vc.color(c_map[0], c_map[1], c_map[2], 1F);
		vc.normal(pose.normal(), (float)vec_N.x(), (float)vec_N.y(), (float)vec_N.z());
		vc.endVertex();

		// end
		vc.vertex(pose.pose(), (float)vec_end.x(), (float)vec_end.y(), (float)vec_end.z());
		vc.color(c_map[0], c_map[1], c_map[2], 1F);
		vc.normal(pose.normal(), (float)vec_N.x(), (float)vec_N.y(), (float)vec_N.z());
		vc.endVertex();
	}

}
