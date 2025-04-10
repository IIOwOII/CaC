package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class IniLogPositionProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		CacModVariables.Log_position = new File((CacModVariables.MapVariables.get(world).Exp_path + "/" + CacModVariables.MapVariables.get(world).Exp_session), File.separator + "log_position.json");
		try {
			CacModVariables.Log_position.getParentFile().mkdirs();
			CacModVariables.Log_position.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		obj_file.add("cac", obj_cac);
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(CacModVariables.Log_position);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
