package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class SimStartProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_trace = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.Exp_trial = 0;
		CacModVariables.Exp_phase = 0;
		CacModVariables.Dat_trial_winlose = 0;
		CacModVariables.Dat_time_gameplay = 0;
		CacModVariables.Dat_pos_time = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_time_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_r_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_r_prep = new com.google.gson.JsonArray();
	}
}
