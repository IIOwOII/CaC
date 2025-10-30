package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class CstRenderComponent {

	public static void renderBar(GuiGraphics gg, int x, int y, int gauge, int gauge_max) {
		gg.blit(new ResourceLocation("cac:textures/screens/texture_bar_frame.png"), x, y, 0, 0, 96, 8, 96, 8);
		gg.blit(new ResourceLocation("cac:textures/screens/texture_bar_gauge.png"), x, y, 0, 0, 64, 8, 96, 8);
	}
	
}
