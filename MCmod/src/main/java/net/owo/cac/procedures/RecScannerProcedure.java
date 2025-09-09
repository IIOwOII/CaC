package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class RecScannerProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_TR = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_absolute = new com.google.gson.JsonArray();
		if (CacModVariables.MapVariables.get(world).Log_type.contains("C")) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Log_scanner));
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
			arr_TR = obj_cac.get("TR").getAsJsonArray();
			arr_absolute = obj_cac.get("absolute").getAsJsonArray();
			arr_TR.add(((int) CacModVariables.MapVariables.get(world).TimS_time));
			arr_absolute.add(((int) CacModVariables.MapVariables.get(world).TimA_time));
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(CacModVariables.Log_scanner);
					fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
