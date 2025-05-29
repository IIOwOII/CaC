
package net.owo.cac.client.screens;

import org.checkerframework.checker.units.qual.h;

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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OvlSurveyOverlay {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
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
			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/gui_blank.png"), 0, 0, 0, 0, w, h, w, h);
			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/texture_slide.png"), w / 2 + -189, h / 2 + 57, 0, 0, 384, 32, 384, 32);

			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/texture_slide_trace.png"), w / 2 + 6, h / 2 + 64, 0, 0, 4, 20, 4, 20);

			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/texture_slide_cursor.png"), w / 2 + 6, h / 2 + 62, 0, 0, 4, 20, 4, 20);

			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/text_winprob.png"), w / 2 + -207, h / 2 + -85, 0, 0, 427, 60, 427, 60);

			event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.cac.ovl_survey.label_label_low"), w / 2 + -190, h / 2 + 40, -1, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.cac.ovl_survey.label_label_mid"), w / 2 + -16, h / 2 + 41, -1, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.cac.ovl_survey.label_label_high"), w / 2 + 147, h / 2 + 38, -1, false);
		}
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
