package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre;
import net.minecraftforge.event.TickEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstSurvey {
	public static boolean IsSurvey = false;
	public static int timer = 0;

	// trial by trial
	public static int[] suv_order = {0,1,2,3,4};
	public static int[] suv_value = {0,0,0,0,0};
	public static int[] suv_value_old = {0,0,0,0,0};

	// quiz by quiz (how many times survey progressed within one trial)
	public static int idx = 0;
	
	@SubscribeEvent
	public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		if (!IsSurvey)
			return;
		
		GuiGraphics gg = event.getGuiGraphics();
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (!IsSurvey)
			return;

		
	}
	
}
