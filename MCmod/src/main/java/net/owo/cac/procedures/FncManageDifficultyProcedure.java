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
		session = CacModVariables.MapVariables.get(world).Exp_session;
		if ((session).equals("test_mixed")) {
			CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = Math.round(Math.pow(10, 2) * (0.8 + 0.02 * Math.floor(CacModVariables.MapVariables.get(world).Exp_trial / 2))) / Math.pow(10, 2);
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Dat_difficulty_relative = CacModVariables.MapVariables.get(world).Dat_difficulty_absolute;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((session).equals("test_chasing") || (session).equals("test_chased")) {
			CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = Math.round(Math.pow(10, 2) * (0.8 + 0.02 * CacModVariables.MapVariables.get(world).Exp_trial)) / Math.pow(10, 2);
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Dat_difficulty_relative = CacModVariables.MapVariables.get(world).Dat_difficulty_absolute;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((session).equals("pseudo_chasing") || (session).equals("pseudo_chased")) {
			if (CacModVariables.MapVariables.get(world).Exp_trial != 0) {
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
				winlose_pre = arr_winlose.get(((int) (CacModVariables.MapVariables.get(world).Exp_trial - 1))).getAsDouble();
				if (winlose_pre == 1) {
					CacModVariables.MapVariables.get(world).Dat_difficulty_relative = Math.round(Math.pow(10, 2) * (CacModVariables.MapVariables.get(world).Dat_difficulty_relative - 0.05)) / Math.pow(10, 2);
					CacModVariables.MapVariables.get(world).syncData(world);
				} else if (winlose_pre == 0) {
					CacModVariables.MapVariables.get(world).Dat_difficulty_relative = Math.round(Math.pow(10, 2) * (CacModVariables.MapVariables.get(world).Dat_difficulty_relative + 0.02)) / Math.pow(10, 2);
					CacModVariables.MapVariables.get(world).syncData(world);
				} else {
					CacModVariables.MapVariables.get(world).Log_error = "invalid_data";
					CacModVariables.MapVariables.get(world).syncData(world);
					CacErrorProcedure.execute(world);
				}
			} else {
				CacModVariables.MapVariables.get(world).Dat_difficulty_relative = 0.5;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = PsyInvpsiLogisticProcedure.execute(world);
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
