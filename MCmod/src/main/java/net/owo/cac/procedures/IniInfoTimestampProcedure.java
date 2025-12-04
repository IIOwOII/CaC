package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniInfoTimestampProcedure {
	public static void execute() {
		com.google.gson.JsonObject obj_timestamp_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_timestamp_sub = new com.google.gson.JsonObject();
		double idx_obj = 0;
		double idx_dup = 0;
		CacModVariables.Info_timestamp = new File(CacModVariables.Dir_behaviors, File.separator + "info_timestamp.json");
		if (!CacModVariables.Info_timestamp.exists()) {
			try {
				CacModVariables.Info_timestamp.getParentFile().mkdirs();
				CacModVariables.Info_timestamp.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_timestamp_sub.addProperty("register_0", Calendar.getInstance().getTime().toString());
			obj_timestamp_main.add("cac", obj_timestamp_sub);
		} else {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Info_timestamp));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj_timestamp_main = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					obj_timestamp_sub = obj_timestamp_main.get("cac").getAsJsonObject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			idx_obj = 0;
			idx_dup = 0;
			for (int index0 = 0; index0 < (int) obj_timestamp_sub.size(); index0++) {
				if (obj_timestamp_sub.keySet().stream().toList().get(((int) idx_obj)).startsWith("register")) {
					idx_dup = idx_dup + 1;
				}
				idx_obj = idx_obj + 1;
			}
			if (idx_dup == 0) {
				obj_timestamp_sub.addProperty("register", Calendar.getInstance().getTime().toString());
			} else {
				obj_timestamp_sub.addProperty(("register_" + (int) idx_dup), Calendar.getInstance().getTime().toString());
			}
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Info_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_timestamp_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
