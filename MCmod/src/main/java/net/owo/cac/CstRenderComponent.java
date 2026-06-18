package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;

import net.owo.cac.CstPsychometric;

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

	public static ResourceLocation scoreboard = new ResourceLocation("cac:textures/screens/texture_scoreboard.png");
	public static ResourceLocation scoreaxis = new ResourceLocation("cac:textures/screens/texture_scoreaxis.png");
	public static ResourceLocation dot_default = new ResourceLocation("cac:textures/screens/dot_default.png");
	public static ResourceLocation dot_lime = new ResourceLocation("cac:textures/screens/dot_lime.png");
	public static ResourceLocation line_r = new ResourceLocation("cac:textures/screens/texture_line_r.png");
	public static ResourceLocation line_ru = new ResourceLocation("cac:textures/screens/texture_line_ru.png");
	public static ResourceLocation line_rd = new ResourceLocation("cac:textures/screens/texture_line_rd.png");
	
	public static ResourceLocation icon_meowcam = new ResourceLocation("cac:textures/screens/icon_meowcam.png");
	public static ResourceLocation icon_meowcam_off = new ResourceLocation("cac:textures/screens/icon_meowcam_off.png");
	
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

	public static void renderPatchFlicker() {
		CstRenderHandler.patch_flicker_timer = 120; // 60fps
	}

	public static void renderPatchToggle() {
		CstRenderHandler.patch_toggle_timer = 60; // 60fps
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
		if (type == 0) {ox = (gw/4)-40;}
		else if (type == 1) {ox = (gw/4)*3-40;}
		else {return;}
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

	// icon
	public static void renderIconMeowcam(GuiGraphics gg) {
		gg.blit(icon_meowcam, 0, 0, 0, 0, 24, 24, 24, 24);
	}
	public static void renderIconMeowcamOff(GuiGraphics gg) {
		gg.blit(icon_meowcam_off, 0, 0, 0, 0, 24, 24, 24, 24);
	}

	// Scoreboard
	public static void renderScoreboard(GuiGraphics gg, int gw, int gh) {
		gg.blit(scoreboard, (gw/2)-192, (gh/2)-108, 0, 0, 384, 216, 384, 216);
		int baseline = (gh/2);
		if (CstPsychometric.score_positive) {
			baseline += 90;
		} else {
			baseline -= 90;
		}
		gg.blit(scoreaxis, (gw/2)-180, baseline-9, 0, 0, 360, 18, 360, 18);
		renderScore(gg, gw, gh, baseline);
	}
	public static void renderScore(GuiGraphics gg, int gw, int gh, int axis_y) {
		int score_size = CstPsychometric.trace_score.size();
		int score_temp = 0;
		int score_diff = 0;
		int x_tick = 10; // 320
		int y_step = 10; // 180
		if (score_size > 1) {
			int score_x_offset = (gw/2) - (score_size-1)*(x_tick/2);
			for (int i=0; i<score_size-1; i++) {
				score_temp = CstPsychometric.trace_score.get(i);
				score_diff = CstPsychometric.trace_score.get(i+1) - score_temp;
				int score_x = score_x_offset + x_tick*i;
				int score_y = axis_y - score_temp*y_step;
				if (score_diff == 1) {
					gg.blit(line_ru, score_x-10, score_y-10, 0, 0, 20, 20, 20, 20);
				} else if (score_diff == 0) {
					gg.blit(line_r, score_x-10, score_y-10, 0, 0, 20, 20, 20, 20);
				} else if (score_diff == -1) {
					gg.blit(line_rd, score_x-10, score_y-10, 0, 0, 20, 20, 20, 20);
				}
				gg.blit(dot_default, score_x-4, score_y-4, 0, 0, 8, 8, 8, 8);
			}
			score_temp = CstPsychometric.trace_score.get(score_size-1);
			gg.blit(dot_lime, score_x_offset+x_tick*(score_size-1)-4, axis_y-score_temp*y_step-4, 0, 0, 8, 8, 8, 8);
		} else if (score_size == 1) {
			score_temp = CstPsychometric.trace_score.get(0);
			gg.blit(dot_lime, (gw/2)-4, axis_y-score_temp*y_step-4, 0, 0, 8, 8, 8, 8);
		}
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
	public static void renderLine(PoseStack ps, VertexConsumer vc, double sx, double sy, double sz, double ex, double ey, double ez, char color) {
		Vec3 vec_start = new Vec3(sx, sy, sz);
		Vec3 vec_end = new Vec3(ex, ey, ez);
		
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

	// Draw Box
	public static void renderBox(PoseStack ps, VertexConsumer vc, Vec3 vec_offset, char color) {
		double x = vec_offset.x();
		double y = vec_offset.y();
		double z = vec_offset.z();

		renderLine(ps, vc, x, y, z, x+1, y, z, color);
		renderLine(ps, vc, x, y, z, x, y, z+1, color);
		renderLine(ps, vc, x+1, y, z, x+1, y, z+1, color);
		renderLine(ps, vc, x, y, z+1, x+1, y, z+1, color);
		
		renderLine(ps, vc, x, y, z, x, y+1, z, color);
		renderLine(ps, vc, x+1, y, z, x+1, y+1, z, color);
		renderLine(ps, vc, x, y, z+1, x, y+1, z+1, color);
		renderLine(ps, vc, x+1, y, z+1, x+1, y+1, z+1, color);
		
		renderLine(ps, vc, x, y+1, z, x+1, y+1, z, color);
		renderLine(ps, vc, x, y+1, z, x, y+1, z+1, color);
		renderLine(ps, vc, x+1, y+1, z, x+1, y+1, z+1, color);
		renderLine(ps, vc, x, y+1, z+1, x+1, y+1, z+1, color);
	}
}
