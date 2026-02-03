package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class IniLogProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
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
				CacModVariables.Exp_property = obj_file.get(CacModVariables.Exp_session).getAsString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (CacModVariables.Exp_property.contains("E")) {
			IniLogEventProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("P")) {
			IniLogPositionProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("G")) {
			IniLogGameplayProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("S")) {
			IniLogSurveyProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("U")) {
			IniLogSurrenderProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("F")) {
			IniLogFittingProcedure.execute();
		}
		if (CacModVariables.Exp_property.contains("C")) {
			IniLogScannerProcedure.execute();
		}
	}
}
