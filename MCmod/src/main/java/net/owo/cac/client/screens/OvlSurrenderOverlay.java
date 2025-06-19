
package net.owo.cac.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.owo.cac.procedures.RtnSurrenderValueRightProcedure;
import net.owo.cac.procedures.RtnSurrenderValueMidProcedure;
import net.owo.cac.procedures.RtnSurrenderValueLeftProcedure;
import net.owo.cac.procedures.RtnSurrenderTypeIProcedure;
import net.owo.cac.procedures.RtnSurrenderTypeIIProcedure;
import net.owo.cac.procedures.RtnSurrenderProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OvlSurrenderOverlay {
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
		if (RtnSurrenderProcedure.execute(world)) {
			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/gui_blank.png"), 0, 0, 0, 0, w, h, w, h);
			if (RtnSurrenderTypeIIProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_no.png"), w / 2 + -144, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderTypeIIProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_yes.png"), w / 2 + 72, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderTypeIProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_no.png"), w / 2 + 72, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderTypeIProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_yes.png"), w / 2 + -144, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderValueMidProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_select.png"), w / 2 + -36, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderValueLeftProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_select.png"), w / 2 + -144, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			if (RtnSurrenderValueRightProcedure.execute(world)) {
				event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/button_select.png"), w / 2 + 72, h / 2 + 23, 0, 0, 80, 32, 80, 32);
			}
			event.getGuiGraphics().blit(new ResourceLocation("cac:textures/screens/text_surrender.png"), w / 2 + -198, h / 2 + -85, 0, 0, 400, 60, 400, 60);

		}
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
