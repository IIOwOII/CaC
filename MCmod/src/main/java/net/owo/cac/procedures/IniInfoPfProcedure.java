package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class IniInfoPfProcedure {
	public static void execute(LevelAccessor world) {
		com.google.gson.JsonObject obj_PF = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_interval = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_step = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_parameter = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_interval_temp = new com.google.gson.JsonArray();
		double step = 0;
		double idx_parameter = 0;
		double step_interval = 0;
		ListTag PF_parameter_temp;
		CacModVariables.MapVariables.get(world).PF_likelihood = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).PF_parameter = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).PF_parameter_name = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.Info_PF = new File(CacModVariables.MapVariables.get(world).Dir_components, File.separator + "info_PF.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Info_PF));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_PF = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				obj_parameter = obj_PF.get("parameter").getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		obj_interval = obj_parameter.get("interval").getAsJsonObject();
		obj_step = obj_parameter.get("step").getAsJsonObject();
		idx_parameter = 0;
		for (int index0 = 0; index0 < (int) obj_interval.size(); index0++) {
			CacModVariables.MapVariables.get(world).PF_parameter_name.addTag((int) idx_parameter, StringTag.valueOf(obj_interval.keySet().stream().toList().get(((int) idx_parameter))));
			idx_parameter = idx_parameter + 1;
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).PF_parameter_name) {
			PF_parameter_temp = new ListTag();
			arr_interval_temp = obj_interval.get((dataelementiterator instanceof StringTag _stringTag ? _stringTag.getAsString() : "")).getAsJsonArray();
			step_interval = obj_step.get((dataelementiterator instanceof StringTag _stringTag ? _stringTag.getAsString() : "")).getAsDouble();
			step = arr_interval_temp.get(0).getAsDouble();
			while (step <= arr_interval_temp.get(1).getAsDouble()) {
				PF_parameter_temp.addTag(PF_parameter_temp.size(), DoubleTag.valueOf(step));
				step = step + step_interval;
			}
			CacModVariables.MapVariables.get(world).PF_parameter.addTag(CacModVariables.MapVariables.get(world).PF_parameter.size(), (PF_parameter_temp.copy()));
		}
	}
}
