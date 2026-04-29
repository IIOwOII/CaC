package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniInfoTimestampProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		double num_rep = 0;
		double num_idx = 0;
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
			num_idx = 0;
			num_rep = 0;
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
			for (int index0 = 0; index0 < (int) obj_cac.size(); index0++) {
				if (obj_cac.keySet().stream().toList().get(((int) num_idx)).startsWith("re-register")) {
					num_rep = num_rep + 1;
				}
				num_idx = num_idx + 1;
			}
			obj_cac.addProperty(("re-register_" + new java.text.DecimalFormat("##").format(num_rep)), Calendar.getInstance().getTime().toString());
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eRe-registered!\u00A7r"), false);
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
