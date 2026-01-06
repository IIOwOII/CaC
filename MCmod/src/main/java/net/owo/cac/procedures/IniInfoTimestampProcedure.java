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
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		CacModVariables.Info_timestamp = new File(CacModVariables.Dir_behaviors, File.separator + "info_timestamp.json");
		if (!CacModVariables.Info_timestamp.exists()) {
			try {
				CacModVariables.Info_timestamp.getParentFile().mkdirs();
				CacModVariables.Info_timestamp.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_cac.addProperty("register", Calendar.getInstance().getTime().toString());
			obj_file.add("cac", obj_cac);
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
					obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					obj_cac = obj_file.get("cac").getAsJsonObject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			obj_cac.addProperty("register", Calendar.getInstance().getTime().toString());
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Info_timestamp);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
