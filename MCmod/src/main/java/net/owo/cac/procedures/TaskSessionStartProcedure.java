package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class TaskSessionStartProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String que_initial = "";
		com.google.gson.JsonObject obj_que = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_session = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		double idx_obj = 0;
		double idx_dup = 0;
		CacModVariables.Info_timestamp = new File(CacModVariables.Dir_behaviors, File.separator + "info_timestamp.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Info_timestamp));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_timestamp = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_cac = obj_timestamp.get("cac").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		idx_obj = 0;
		idx_dup = 0;
		for (int index0 = 0; index0 < (int) obj_cac.size(); index0++) {
			if (obj_que.keySet().stream().toList().get(((int) idx_obj)).startsWith(CacModVariables.Exp_session + "_start")) {
				idx_dup = idx_dup + 1;
			}
			idx_obj = idx_obj + 1;
		}
		if (idx_dup == 0) {
			obj_cac.addProperty((CacModVariables.Exp_session + "_start"), Calendar.getInstance().getTime().toString());
		} else {
			obj_cac.addProperty((CacModVariables.Exp_session + "_start_" + (int) idx_dup), Calendar.getInstance().getTime().toString());
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Info_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_timestamp));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		EvResetProcedure.execute(world);
		TimResetProcedure.execute(world);
		IniLogProcedure.execute();
		IniQueProcedure.execute(world);
		EvQueCallProcedure.execute(world, x, y, z, entity);
	}
}
