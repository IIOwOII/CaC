package net.owo.cac;

import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;

import net.owo.cac.CstState;
import net.owo.cac.CstRenderComponent;
import net.owo.cac.network.CacModVariables;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstSurrender {
	static ResourceLocation surrender_text = new ResourceLocation("cac:textures/screens/text_surrender.png");
	
	public static boolean IsSurrender = false;
	public static int sur_type = 0; // even trial(0,2,4,...) = 0, odd trial(1,3,5,...) = 1, not recorded
	public static int sur_time = 0;
	public static double sur_select = 0.5; // left = 0, right = 1, init = 0.5
	public static int sur_answer = 0; // yes = 1, no = 0 (transformed by sur_select)
	
	@SubscribeEvent
	public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		if (!IsSurrender) return;
		
		GuiGraphics gg = event.getGuiGraphics();
		CstRenderComponent.renderBlank(gg); // Render Background
		gg.blit(surrender_text, 13, 30, 0, 0, 400, 60, 400, 60); // Render Text

		// left button : x=70, y=140 (OR) right button : x=285, y=140
		if (sur_type == 0) { // yes is left
			CstRenderComponent.renderButtonYes(gg, 70, 140);
			CstRenderComponent.renderButtonNo(gg, 285, 140);
		} else if (sur_type == 1) { // no is left
			CstRenderComponent.renderButtonYes(gg, 285, 140);
			CstRenderComponent.renderButtonNo(gg, 70, 140);
		}

		// selection highlight
		CstRenderComponent.renderButtonSelect(gg, (int)(70 + 215 * sur_select), 140);
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (!IsSurrender) return;
		if (event.phase == TickEvent.Phase.END) {
			sur_time = sur_time + 1;
			if (CstState.key_pressed[0])
				sur_select = 1;
			if (CstState.key_pressed[1])
				sur_select = 0;
			if ((CstState.key_pressed[5]) && (sur_select == 0 || sur_select == 1)) {
				confirmSurrender();
			}
		}
	}

	// reset (initialize: use when debugging or start experiment)
	public static void initSurrender() {
		IsSurrender = false;
		sur_time = 0;
		sur_select = 0.5;
	}

	// trial by trial OR surrender by surrender
	public static void startSurrender() {
		CstState.offMeowMove(); // stop moving
		sur_type = (int)(CacModVariables.Exp_trial % 2);
		sur_time = 0;
		sur_select = 0.5;
		IsSurrender = true;
	}
	
	public static void endSurrender() {
		IsSurrender = false;
		recordSurrender();
		CstState.onMeowMove(); // start moving
	}
	
	public static void confirmSurrender() {
		if ((sur_type == 0 && sur_select == 0) || (sur_type == 1 && sur_select == 1)) { // Yes
			sur_answer = 1;
		} else if ((sur_type == 0 && sur_select == 1) || (sur_type == 1 && sur_select == 0)) { // No
			sur_answer = 0;
		}
		endSurrender();
	}

	// Recording Data on log file
	public static void recordSurrender() {
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_trial = new JsonObject();
		Gson GS = new Gson();
		
		if (CacModVariables.Log_type.contains("U")) {
			// Read Log file and get cac jsonobject
			try {
				BufferedReader BR = new BufferedReader(new FileReader(CacModVariables.Log_surrender));
				StringBuilder SB = new StringBuilder();
				String line;
				while ((line = BR.readLine()) != null) {
					SB.append(line);
				}
				BR.close();
				obj_file = GS.fromJson(SB.toString(), JsonObject.class);
				obj_cac = obj_file.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
			obj_trial.addProperty("time", sur_time);
			obj_trial.addProperty("answer", sur_answer);
			obj_cac.add(("trial_" + (int)CacModVariables.Exp_trial), obj_trial);
			// Write file
			Gson GB = new GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_surrender);
				fileWriter.write(GB.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
}
