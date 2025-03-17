package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.File;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		File file = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
		file = new File(CacModVariables.MapVariables.get(world).Exp_path, File.separator + "test.json");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
