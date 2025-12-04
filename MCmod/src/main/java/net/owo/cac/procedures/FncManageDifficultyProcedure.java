package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class FncManageDifficultyProcedure {
	public static void execute(LevelAccessor world) {
		String session = "";
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_winlose = new com.google.gson.JsonArray();
		double winlose_pre = 0;
		session = CacModVariables.Exp_session;
		if ((session).equals("test_mixed")) {
			CacModVariables.Dat_difficulty_absolute = Math.round(Math.pow(10, 2) * (0.9 + 0.02 * Math.floor(CacModVariables.Exp_trial / 2))) / Math.pow(10, 2);
			CacModVariables.Dat_difficulty_relative = CacModVariables.Dat_difficulty_absolute;
		} else if ((session).equals("test_chasing") || (session).equals("test_chased")) {
			CacModVariables.Dat_difficulty_absolute = Math.round(Math.pow(10, 2) * (0.9 + 0.02 * CacModVariables.Exp_trial)) / Math.pow(10, 2);
			CacModVariables.Dat_difficulty_relative = CacModVariables.Dat_difficulty_absolute;
		} else if ((session).equals("pseudo_chasing") || (session).equals("pseudo_chased")) {
			if (CacModVariables.Exp_trial != 0) {
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_gameplay));
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
				arr_winlose = obj_cac.get("winlose").getAsJsonArray();
				winlose_pre = arr_winlose.get(((int) (CacModVariables.Exp_trial - 1))).getAsDouble();
				if (winlose_pre == 1) {
					CacModVariables.Dat_difficulty_relative = Math.round(Math.pow(10, 2) * (CacModVariables.Dat_difficulty_relative - 0.05)) / Math.pow(10, 2);
				} else if (winlose_pre == 0) {
					CacModVariables.Dat_difficulty_relative = Math.round(Math.pow(10, 2) * (CacModVariables.Dat_difficulty_relative + 0.02)) / Math.pow(10, 2);
				} else {
					CacModVariables.Log_error = "invalid_data";
					CacErrorProcedure.execute(world);
				}
			} else {
				CacModVariables.Dat_difficulty_relative = 0.5;
			}
			CacModVariables.Dat_difficulty_absolute = PsyInvpsiLogisticProcedure.execute();
		}
	}
}
