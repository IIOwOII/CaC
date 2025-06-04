package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniPoolSurveyProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_survey = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_survey_name = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_range = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_label = new com.google.gson.JsonArray();
		double idx = 0;
		double total_number = 0;
		String name = "";
		CacModVariables.Pool_survey = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_survey.json");
		CacModVariables.MapVariables.get(world).Suv_reference = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_type = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_range_lower = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_range_upper = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_label_low = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_label_mid = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_label_high = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Suv_initial = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		idx = 0;
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_survey));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_survey = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				total_number = obj_survey.size();
				for (int index0 = 0; index0 < (int) total_number; index0++) {
					name = obj_survey.keySet().stream().toList().get(((int) idx));
					obj_survey_name = obj_survey.get(name).getAsJsonObject();
					CacModVariables.MapVariables.get(world).Suv_reference.addTag((int) idx, StringTag.valueOf(name));
					CacModVariables.MapVariables.get(world).Suv_type.addTag((int) idx, StringTag.valueOf(obj_survey_name.get("type").getAsString()));
					arr_range = obj_survey_name.get("range").getAsJsonArray();
					CacModVariables.MapVariables.get(world).Suv_range_lower.addTag((int) idx, DoubleTag.valueOf(arr_range.get(0).getAsDouble()));
					CacModVariables.MapVariables.get(world).Suv_range_upper.addTag((int) idx, DoubleTag.valueOf(arr_range.get(1).getAsDouble()));
					arr_label = obj_survey_name.get("label").getAsJsonArray();
					CacModVariables.MapVariables.get(world).Suv_label_low.addTag((int) idx, StringTag.valueOf(arr_label.get(0).getAsString()));
					CacModVariables.MapVariables.get(world).Suv_label_mid.addTag((int) idx, StringTag.valueOf(arr_label.get(1).getAsString()));
					CacModVariables.MapVariables.get(world).Suv_label_high.addTag((int) idx, StringTag.valueOf(arr_label.get(2).getAsString()));
					CacModVariables.MapVariables.get(world).Suv_initial.addTag((int) idx, DoubleTag.valueOf(obj_survey_name.get("initial").getAsDouble()));
					idx = idx + 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
