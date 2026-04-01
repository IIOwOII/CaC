package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class FncResetDatPosProcedure {
	public static void execute() {
		CacModVariables.Dat_pos_time = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_time_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_prey_r_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_predator_r_prep = new com.google.gson.JsonArray();
	}
}
