package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderComponent {
	public static ResourceLocation bar_frame = new ResourceLocation("cac:textures/screens/texture_bar_frame.png");
	public static ResourceLocation bar_gauge = new ResourceLocation("cac:textures/screens/texture_bar_gauge.png");
	public static ResourceLocation patch_white = new ResourceLocation("cac:textures/screens/texture_patch_white.png");
	public static ResourceLocation patch_black = new ResourceLocation("cac:textures/screens/texture_patch_black.png");

	public static void renderBar(GuiGraphics gg, int x, int y, float ratio) {
		int gauge = (int) (96F*ratio);
		gg.blit(bar_frame, x, y, 0, 0, 96, 8, 96, 8);
		gg.blit(bar_gauge, x, y, 0, 0, gauge, 8, 96, 8);
	}

	public static void renderPatchWhite(GuiGraphics gg) {
		gg.blit(patch_white, 427-64, 0, 0, 0, 64, 64, 64, 64);
	}

	public static void renderPatchBlack(GuiGraphics gg) {
		gg.blit(patch_black, 427-64, 0, 0, 0, 64, 64, 64, 64);
	}
	
}
