package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class PsyQuestInitializeProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_quest = new com.google.gson.JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_psychometric));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_quest = obj_file.get("QUEST").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.Psy_quest_param_initial = obj_quest.get("initial").getAsJsonArray();
		CacModVariables.Psy_quest_param_min = obj_quest.get("min").getAsJsonArray();
		CacModVariables.Psy_quest_param_max = obj_quest.get("max").getAsJsonArray();
		CacModVariables.Psy_quest_param_step = obj_quest.get("step").getAsJsonArray();
		CacModVariables.Psy_quest_gridsize = obj_quest.get("gridsize").getAsJsonArray();
		CacModVariables.Psy_quest_L = new ListTag();
		for (int index0 = 0; index0 < (int) (CacModVariables.Psy_quest_gridsize.get(0).getAsDouble() * CacModVariables.Psy_quest_gridsize.get(1).getAsDouble() * CacModVariables.Psy_quest_gridsize.get(2).getAsDouble()
				* CacModVariables.Psy_quest_gridsize.get(3).getAsDouble()); index0++) {
			CacModVariables.Psy_quest_L.addTag(CacModVariables.Psy_quest_L.size(), DoubleTag.valueOf(0));
		}
	}
}
