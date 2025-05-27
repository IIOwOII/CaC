package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniPoolSurveyProcedure {
	public static void execute(LevelAccessor world) {
		ListTag suv_range;
		ListTag suv_label;
		double idx_survey = 0;
		double num_survey = 0;
		com.google.gson.JsonObject obj_survey = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_survey_name = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_range = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_label = new com.google.gson.JsonArray();
		String name_survey = "";
		CacModVariables.Pool_survey = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "pool_survey.json");
		CacModVariables.MapVariables.get(world).List_survey_name = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_survey_type = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_survey_range = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_survey_label = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_survey_initial = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		idx_survey = 0;
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
				num_survey = obj_survey.size();
				for (int index0 = 0; index0 < (int) num_survey; index0++) {
					name_survey = obj_survey.keySet().stream().toList().get(((int) idx_survey));
					obj_survey_name = obj_survey.get(name_survey).getAsJsonObject();
					CacModVariables.MapVariables.get(world).List_survey_name.addTag((int) idx_survey, StringTag.valueOf(name_survey));
					CacModVariables.MapVariables.get(world).List_survey_type.addTag((int) idx_survey, StringTag.valueOf(obj_survey_name.get("type").getAsString()));
					arr_range = obj_survey_name.get("range").getAsJsonArray();
					suv_range = new ListTag();
					suv_range.addTag(0, IntTag.valueOf((int) arr_range.get(0).getAsDouble()));
					suv_range.addTag(1, IntTag.valueOf((int) arr_range.get(1).getAsDouble()));
					CacModVariables.MapVariables.get(world).List_survey_range.addTag((int) idx_survey, (suv_range.copy()));
					arr_label = obj_survey_name.get("label").getAsJsonArray();
					suv_label = new ListTag();
					suv_label.addTag(0, StringTag.valueOf(arr_label.get(0).getAsString()));
					suv_label.addTag(1, StringTag.valueOf(arr_label.get(1).getAsString()));
					suv_label.addTag(2, StringTag.valueOf(arr_label.get(2).getAsString()));
					CacModVariables.MapVariables.get(world).List_survey_label.addTag((int) idx_survey, (suv_label.copy()));
					CacModVariables.MapVariables.get(world).List_survey_initial.addTag((int) idx_survey, IntTag.valueOf((int) obj_survey_name.get("initial").getAsDouble()));
					idx_survey = idx_survey + 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
