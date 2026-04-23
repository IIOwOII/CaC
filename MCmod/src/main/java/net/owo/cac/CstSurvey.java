package net.owo.cac;

import com.google.gson.JsonArray;
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
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;

import net.owo.cac.CstState;
import net.owo.cac.CstRenderComponent;
import net.owo.cac.CstTutorial;
import net.owo.cac.network.CacModVariables;

import net.owo.cac.procedures.EvPulseRecordProcedure;
import net.owo.cac.procedures.EvQueImmediateProcedure;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstSurvey {
	/*
	private static final String[] SUV_TYPE = {
		"winprob", "perdiff", "stress", "target", "control"};
	static ResourceLocation survey_winprob = new ResourceLocation("cac:textures/screens/text_winprob.png");
	static ResourceLocation survey_perdiff = new ResourceLocation("cac:textures/screens/text_perdiff.png");
	static ResourceLocation survey_stress = new ResourceLocation("cac:textures/screens/text_stress.png");
	static ResourceLocation survey_target = new ResourceLocation("cac:textures/screens/text_target.png");
	static ResourceLocation survey_control = new ResourceLocation("cac:textures/screens/text_control.png");
	*/
	/*
	static ResourceLocation[] SURVEY_LIST = {
		new ResourceLocation("cac:textures/screens/text_survey_1.png"),
		new ResourceLocation("cac:textures/screens/text_survey_2.png"),
		new ResourceLocation("cac:textures/screens/text_survey_3.png"),
		new ResourceLocation("cac:textures/screens/text_survey_4.png"),
		new ResourceLocation("cac:textures/screens/text_survey_5.png"),
		new ResourceLocation("cac:textures/screens/text_survey_6.png"),
		new ResourceLocation("cac:textures/screens/text_survey_7.png"),
		new ResourceLocation("cac:textures/screens/text_survey_8.png")
	};
	*/
	static ResourceLocation[] SURVEY_LIST = {
		new ResourceLocation("cac:textures/screens/text_survey_short_1.png"),
		new ResourceLocation("cac:textures/screens/text_survey_short_2.png"),
		new ResourceLocation("cac:textures/screens/text_survey_short_3.png"),
		new ResourceLocation("cac:textures/screens/text_survey_short_4.png")
	};
	
	public static boolean IsSurvey = false;
	public static int timer_quiz = 0;
	public static int timer_blank = 0;
	public static int suv_phase = 30;

	// trial by trial
	public static int[] suv_order;
	public static int[] suv_value;
	public static int[] suv_value_prev;
	public static int[] suv_time;

	// quiz by quiz (how many times survey progressed within one trial)
	public static int idx = 0;
	
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Pre event) {
		if (!IsSurvey) return;
		GuiGraphics gg = event.getGuiGraphics();
		int gw = event.getWindow().getGuiScaledWidth();
		int gh = event.getWindow().getGuiScaledHeight();
		CstRenderComponent.renderBlank(gg, gw, gh); // Render Cross blank
		if (suv_phase == 35) {
			CstRenderComponent.renderBlankLightgrey(gg, gw, gh);
			int suv_id = suv_order[idx];
			CstRenderComponent.renderBar(gg, gw, gh, 200-timer_quiz, 200); // Render timebar
			renderSurvey(gg, gw, gh, suv_id); // Render text
			CstRenderComponent.renderSlide(gg, gw, gh, suv_value[suv_id], suv_value_prev[suv_id], 100); // Render Slide
			CstRenderComponent.renderSlideText(gg, gw, gh); // Render Slide Text
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (!IsSurvey) return;
		if (event.phase == TickEvent.Phase.END) {
			if (suv_phase == 35) {
				timer_quiz = timer_quiz + 1;
				int suv_id = suv_order[idx];
				if ((CstState.key_pressed[0]) && (suv_value[suv_id] < 100))
					suv_value[suv_id] = suv_value[suv_id] + 1;
				if ((CstState.key_pressed[1]) && (suv_value[suv_id] > 0))
					suv_value[suv_id] = suv_value[suv_id] - 1;
				if ((CstState.key_pressed[5]) || (timer_quiz >= 200))
					confirmSurvey();
			} else if (suv_phase == 33) {
				timer_blank = timer_blank + 1;
				if (timer_blank >= 20) {
					progressSurvey();
				}
			}
		}
	}

	public static void randomizeOrder() {
		
	}

	public static void renderSurvey(GuiGraphics gg, int gw, int gh, int ID) {
		int ox = gw/2 - 200;
		gg.blit(SURVEY_LIST[ID], ox, 60, 0, 0, 400, 60, 400, 60);
	}

	// reset (initialize: use when debugging or start experiment)
	public static void initSurvey() {
		IsSurvey = false;
		timer_quiz = 0;
		timer_blank = 0;
		suv_order = new int[SURVEY_LIST.length];
		suv_value = new int[SURVEY_LIST.length];
		suv_value_prev = new int[SURVEY_LIST.length];
		suv_time = new int[SURVEY_LIST.length];
		for(int i=0; i<SURVEY_LIST.length; i++) {
			suv_order[i] = i;
			suv_value[i] = 50;
			suv_value_prev[i] = 50;
			suv_time[i] = 0;
		}
		idx = 0;
	}
	
	// trial by trial
	public static void startSurvey() { // phase 3.0
		CstState.offMeowMove(); // stop moving
		suv_phase = 30;
		idx = 0;
		randomizeOrder();
		suv_value_prev = suv_value.clone();
		for(int i=0;i<SURVEY_LIST.length;i++) {
			suv_value[i] = 50;
		}
		timer_blank = 0;
		IsSurvey = true;
		waitingSurvey();
	}
	public static void endSurvey() {
		suv_phase = 30; // phase reset
		recordSurvey();
		if (CstTutorial.tuto_id == 30) { // is tutorial?
			CstTutorial.completeMission(6, true);
		}
		EvQueImmediateProcedure.execute(); // next event
		IsSurvey = false;
	}

	// quiz by quiz
	public static void waitingSurvey() { // phase 3.3
		suv_phase = 33; // start of func
		timer_quiz = 0;
		CacModVariables.Ev_pulse_content = ("survey_waiting_" + idx);
		EvPulseRecordProcedure.execute();
	}
	public static void progressSurvey() { // phase 3.5
		suv_phase = 35; // end of func
		timer_blank = 0;
		CacModVariables.Ev_pulse_content = ("survey_progress_" + idx);
		EvPulseRecordProcedure.execute();
	}
	public static void confirmSurvey() { // phase 3.7
		suv_phase = 37; // start of func

		// Intermediate Record
		int suv_id = suv_order[idx];
		suv_time[suv_id] = timer_quiz;

		// Event Log Record
		CacModVariables.Ev_pulse_content = ("survey_confirm_" + idx);
		EvPulseRecordProcedure.execute();

		// Loop or end
		idx = idx + 1;
		if (idx < SURVEY_LIST.length) {
			waitingSurvey();
		} else {
			endSurvey();
		}
	}

	// Recording data on log file
	public static void recordSurvey() {
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		JsonObject obj_trial = new JsonObject();
		JsonArray arr_order = new JsonArray();
		JsonArray arr_time = new JsonArray();
		JsonArray arr_answer = new JsonArray();

		Gson GS = new Gson();
		
		if (CacModVariables.Exp_property.contains("S")) {
			// Read Log file and get cac jsonobject
			try {
				BufferedReader BR = new BufferedReader(new FileReader(CacModVariables.Log_survey));
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
			// Data Stack
			for (int i=0; i<SURVEY_LIST.length; i++) {
				arr_order.add(suv_order[i]);
				arr_time.add(suv_time[i]);
				arr_answer.add(suv_value[i]);
			}
			obj_trial.add("order", arr_order);
			obj_trial.add("time", arr_time);
			obj_trial.add("answer", arr_answer);
			obj_cac.add(("trial_" + (int)CacModVariables.Exp_trial), obj_trial);
			// Write file
			Gson GB = new GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_survey);
				fileWriter.write(GB.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
}
