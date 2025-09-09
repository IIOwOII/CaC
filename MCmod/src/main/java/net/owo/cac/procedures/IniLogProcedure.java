package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniLogProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		String log_type = "";
		CacModVariables.MapVariables.get(world).Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.MapVariables.get(world).Exp_subject;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Exp_session_reps == 0) {
			CacModVariables.MapVariables.get(world).Dir_behaviors_session = CacModVariables.MapVariables.get(world).Dir_behaviors + "/" + CacModVariables.MapVariables.get(world).Exp_session;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Dir_behaviors_session = CacModVariables.MapVariables.get(world).Dir_behaviors + "/" + CacModVariables.MapVariables.get(world).Exp_session + "_"
					+ CacModVariables.MapVariables.get(world).Exp_session_reps;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_task));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				log_type = obj_file.get(CacModVariables.MapVariables.get(world).Exp_session).getAsString();
				CacModVariables.MapVariables.get(world).Log_type = obj_file.get(CacModVariables.MapVariables.get(world).Exp_session).getAsString();
				CacModVariables.MapVariables.get(world).syncData(world);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (log_type.contains("E")) {
			IniLogEventProcedure.execute(world);
		}
		if (log_type.contains("P")) {
			IniLogPositionProcedure.execute(world);
		}
		if (log_type.contains("G")) {
			IniLogGameplayProcedure.execute(world);
		}
		if (log_type.contains("S")) {
			IniLogSurveyProcedure.execute(world);
		}
		if (log_type.contains("C")) {
			IniLogScannerProcedure.execute(world);
		}
	}
}
