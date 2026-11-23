package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstEyetrack {
	public static ResourceLocation text_here = new ResourceLocation("cac:textures/screens/texture_here.png");
	
	public static boolean switch_eyetrack = false;
	public static int signal_count = 0;
	
	// Rendering
	@SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
    	GuiGraphics gg = event.getGuiGraphics();
	    int ww = event.getWindow().getWidth();
		int wh = event.getWindow().getHeight();
		int gw = event.getWindow().getGuiScaledWidth();
		int gh = event.getWindow().getGuiScaledHeight();
    }
    

	// Basic Utils
	public static void resetEyetrack() {
		setSwitchEyetrack(false);
		signal_count = 0;
	}

	public static void setSwitchEyetrack(boolean sw) {
		switch_eyetrack = sw;
	}

	public static int[] getGridPosition(int id, int ww, int wh) {
		// order : (1,1), (3,2), (2,0), (0,2), (2,1), (0,0), (3,1), (1,2), (3,0), (2,2), (0,1), (1,0) * 2
		int[][] grid_order = {{1,1}, {3,2}, {2,0}, {0,2}, {2,1}, {0,0}, {3,1}, {1,2}, {3,0}, {2,2}, {0,1}, {1,0}};
		
		// seperate (8,6) grid
		// x = 1,3,5,7
		// y = 1,3,5
		int wwi = ww / 8; // width interval
		int whi = wh / 6; // height interval
		int wwo = ww / 2; // width offset (center)
		int who = wh / 2; // height offset (center)

		int[] w_pos = {wwo-wwi*3, wwo-gwi, wwo+wwi, wwo+wwi*3};
		int[] h_pos = {who-whi*2, who, who+whi*2};
		
	    int[] grid_pos = {w_pos[grid_order[id][0]], h_pos[grid_order[id][1]]};
	    return grid_pos;
	}
	
	// Eyetracking
    public static void startEyetrack(GuiGraphics gg, int ww, int wh) {
    	signal_count = 0;
    	int text_pos = getGridPosition(0, ww, wh);
    }

    // render
	// Draw Rect (Eyetrack)
	public static void renderEyerect(GuiGraphics gg, int ww, int wh, int ox, int oy, int count, int period, int ms) {
		// period = 1000 (default) (ms) [period = half-TR]
		// count : The number of TRs after starting Eyetracking
		int frame_max = 50; // Recommend FPS >= 50
		int size_max = ww/12; // original size : 160px in 1920x1080 => 1:12 (size:width)
		int size_offset = ww/96; // original size : 20px in 1920x1080 => 1:96 (size:width)
		int ms_per_frame = period/frame_max;
		int frame = ms/ms_per_frame;
		int size = (size_max*frame)/frame_max;
		gg.fill(ox - size, oy - size, size, size, 0xFF00FF00); // Green
		gg.fill(ox - size_offset, oy - size_offset, size_offset, size_offset, 0xFFFF0000); // Red
	}
}
