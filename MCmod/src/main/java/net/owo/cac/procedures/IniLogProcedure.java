package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniLogProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		String log_type = "";
		CacModVariables.Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.Exp_subject;
		CacModVariables.Dir_behaviors_session = CacModVariables.Dir_behaviors + "/" + CacModVariables.Exp_session;
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
				log_type = obj_file.get(CacModVariables.Exp_session).getAsString();
				CacModVariables.Log_type = obj_file.get(CacModVariables.Exp_session).getAsString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (log_type.contains("E")) {
			IniLogEventProcedure.execute();
		}
		if (log_type.contains("P")) {
			IniLogPositionProcedure.execute();
		}
		if (log_type.contains("G")) {
			IniLogGameplayProcedure.execute();
		}
		if (log_type.contains("S")) {
			IniLogSurveyProcedure.execute();
		}
		if (log_type.contains("U")) {
			IniLogSurrenderProcedure.execute();
		}
		if (log_type.contains("F")) {
			IniLogFittingProcedure.execute();
		}
		if (log_type.contains("C")) {
			IniLogScannerProcedure.execute();
		}
	}
}
