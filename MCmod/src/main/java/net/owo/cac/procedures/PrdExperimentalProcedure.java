package net.owo.cac.procedures;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class PrdExperimentalProcedure {
	public static void execute() {
		File file = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/cacutil/test"), File.separator + "tt.json");
		if (file.exists()) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj_main = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					obj_sub.addProperty("huhu", "hi");
					obj_main.add("hoho", obj_sub);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_sub.addProperty("nono", "hi");
			obj_main.add("nunu", obj_sub);
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
