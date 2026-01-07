package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class PrdExperimentalProcedure {
	public static void execute() {
		File file_test = new File("");
		com.google.gson.JsonArray arr_test = new com.google.gson.JsonArray();
		double idx = 0;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		file_test = new File(CacModVariables.Dir_components, File.separator + "log_test.json");
		try {
			file_test.getParentFile().mkdirs();
			file_test.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		PsyQuestPriorProcedure.execute();
		idx = 0;
		for (int index0 = 0; index0 < CacModVariables.Psy_quest_L.size(); index0++) {
			arr_test.add(((CacModVariables.Psy_quest_L.get((int) idx)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D));
			idx = idx + 1;
		}
		obj_file.add("test", arr_test);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(file_test);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
