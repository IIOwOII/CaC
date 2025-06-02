
package net.owo.cac.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.procedures.RtnBlankProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OvlSurveyOverlay {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = mc.player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		if (RtnBlankProcedure.execute(world)) {
			
			GuiGraphics gg = event.getGuiGraphics();
			
			String suv_name = CacModVariables.MapVariables.get(world).Dat_survey_name;
			String suv_type = CacModVariables.MapVariables.get(world).Dat_survey_type;
			int suv_value = CacModVariables.MapVariables.get(world).Dat_survey_value;
			int suv_value_pre = CacModVariables.MapVariables.get(world).Dat_survey_value_pre;
			double suv_range = (double) (CacModVariables.MapVariables.get(world).Dat_survey_range_upper - CacModVariables.MapVariables.get(world).Dat_survey_range_lower);

			// Background
			gg.blit(new ResourceLocation("cac:textures/screens/gui_blank.png"), 0, 0, 0, 0, w, h, w, h);

			// Slide
			if (suv_type == "slide") {
				gg.blit(new ResourceLocation("cac:textures/screens/texture_slide.png"), (w/2-200), (h/2+54), 0, 0, 400, 32, 400, 32);
				gg.blit(new ResourceLocation("cac:textures/screens/texture_slide_trace.png"), (w/2-2), (h/2+60), 0, 0, 4, 20, 4, 20);
				gg.blit(new ResourceLocation("cac:textures/screens/texture_slide_cursor.png"), (w/2-2), (h/2+60), 0, 0, 4, 20, 4, 20);
			}

			// Label
			gg.drawString(mc.font, Component.translatable("gui.cac.ovl_survey.label_label_low"), (w/2-200), (h/2+40), -1, false);
			gg.drawString(mc.font, Component.translatable("gui.cac.ovl_survey.label_label_mid"), (w/2), (h/2+40), -1, false);
			gg.drawString(mc.font, Component.translatable("gui.cac.ovl_survey.label_label_high"), (w/2+200), (h/2+40), -1, false);

			// Survey
			StringBuilder sb_name = new StringBuilder("cac:textures/screens/text_");
			sb_name.append(suv_name);
			sb_name.append(".png");
			gg.blit(new ResourceLocation(sb_name.toString()), w/2-200, h/2-90, 0, 0, 400, 60, 400, 60);
			
		}
		
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
