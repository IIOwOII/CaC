package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecPositionProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_trial = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_player = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_opponent = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_gameplay = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_preparation = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_player_prep = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_opponent_prep = new com.google.gson.JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_position));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_cac = obj_file.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!CacModVariables.Dat_pos_time_prep.isEmpty()) {
			obj_player_prep.add("x", CacModVariables.Dat_pos_player_x_prep);
			obj_player_prep.add("z", CacModVariables.Dat_pos_player_z_prep);
			obj_player_prep.add("r", CacModVariables.Dat_pos_player_r_prep);
			obj_opponent_prep.add("x", CacModVariables.Dat_pos_opponent_x_prep);
			obj_opponent_prep.add("z", CacModVariables.Dat_pos_opponent_z_prep);
			obj_opponent_prep.add("r", CacModVariables.Dat_pos_opponent_r_prep);
			obj_preparation.add("time", CacModVariables.Dat_pos_time_prep);
			obj_preparation.add("player", obj_player_prep);
			obj_preparation.add("opponent", obj_opponent_prep);
			obj_trial.add("preparation", obj_preparation);
		}
		if (!CacModVariables.Dat_pos_time.isEmpty()) {
			obj_player.add("x", CacModVariables.Dat_pos_player_x);
			obj_player.add("z", CacModVariables.Dat_pos_player_z);
			obj_player.add("r", CacModVariables.Dat_pos_player_r);
			obj_opponent.add("x", CacModVariables.Dat_pos_opponent_x);
			obj_opponent.add("z", CacModVariables.Dat_pos_opponent_z);
			obj_opponent.add("r", CacModVariables.Dat_pos_opponent_r);
			obj_gameplay.add("time", CacModVariables.Dat_pos_time);
			obj_gameplay.add("player", obj_player);
			obj_gameplay.add("opponent", obj_opponent);
			obj_trial.add("gameplay", obj_gameplay);
		}
		obj_cac.add(("trial_" + (int) CacModVariables.MapVariables.get(world).Exp_trial), obj_trial);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_position);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
