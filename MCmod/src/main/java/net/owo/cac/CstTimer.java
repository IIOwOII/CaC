package net.owo.cac;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.ArrayList;

import net.minecraft.world.level.LevelAccessor;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;

import net.owo.cac.CacMod;
import net.owo.cac.CstState;
import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstTimer {
	public static boolean switch_timer = false;
	
	public static long TimR = 0; // Relative (1 sec = 20 tick)
	public static long TimA = 0; // Absolute (millisecond)
	public static long TimA_cur = 0;
	public static long TimA_old = 0;

	public static ArrayList<Long> action_TimA = new ArrayList<>();
	public static ArrayList<Long> action_TimR = new ArrayList<>();
	public static ArrayList<Integer> action_stick = new ArrayList<>();
	public static ArrayList<Integer> action_select = new ArrayList<>();
	public static ArrayList<Integer> action_signal = new ArrayList<>();
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		LevelAccessor world = event.player.level();
		if (world.isClientSide() || !switch_timer) return;
		if (event.phase == TickEvent.Phase.END) {
			// TimR calculate
			TimR++;
			// TimA calculate
			TimA_cur = Calendar.getInstance().getTimeInMillis();
			TimA += (TimA_cur - TimA_old);
			TimA_old = TimA_cur;
			// Log
			action_TimA.add(TimA);
			action_TimR.add(TimR);
			action_stick.add(CstState.getKeyCase());
			action_select.add(CstState.key_pressed[5]?1:0);
			action_signal.add(CstState.key_pressed[7]?1:0);
		}
	}

	// Record Action log
	public static void recordAction() {
		Gson GS = new Gson();
		JsonObject obj_file = new JsonObject();
		JsonObject obj_cac = new JsonObject();
		
		if (CacModVariables.Exp_property.contains("A")) {
			JsonArray arr_TimA = GS.toJsonTree(action_TimA.clone()).getAsJsonArray();
			JsonArray arr_TimR = GS.toJsonTree(action_TimR.clone()).getAsJsonArray();
			JsonArray arr_stick = GS.toJsonTree(action_stick.clone()).getAsJsonArray();
			JsonArray arr_select = GS.toJsonTree(action_select.clone()).getAsJsonArray();
			JsonArray arr_signal = GS.toJsonTree(action_signal.clone()).getAsJsonArray();
			try {
				BufferedReader BR = new BufferedReader(new FileReader(CacModVariables.Log_action));
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
			obj_cac.add("time", arr_TimA);
			obj_cac.add("tick", arr_TimR);
			obj_cac.add("stick", arr_stick);
			obj_cac.add("select", arr_select);
			obj_cac.add("signal", arr_signal);
			Gson GB = new GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_action);
				fileWriter.write(GB.toJson(obj_file));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Timer Switch
	public static void onTimer() {
		// TimR reset
		TimR = 0;
		// TimA reset
		TimA = 0;
		TimA_old = Calendar.getInstance().getTimeInMillis();
		// Key reset
		action_TimA.clear();
		action_TimR.clear();
		action_stick.clear();
		action_select.clear();
		action_signal.clear();
		// switch on
		switch_timer = true;
	}
	public static void offTimer() {
		switch_timer = false;
	}
}
