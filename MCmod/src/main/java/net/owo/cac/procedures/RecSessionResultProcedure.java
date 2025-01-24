package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ByteTag;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class RecSessionResultProcedure {
	public static void execute(LevelAccessor world) {
		File file_beh = new File("");
		double idx = 0;
		ListTag lst_type_survey_temp;
		com.google.gson.JsonObject obj_session = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject subobj_type = new com.google.gson.JsonObject();
		com.google.gson.JsonObject subobj_time = new com.google.gson.JsonObject();
		com.google.gson.JsonObject subobj_gameplay = new com.google.gson.JsonObject();
		com.google.gson.JsonObject subobj_survey = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_time_preparation = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_spawn_opponent = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_type_trial = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_time_gameplay = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_win = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_type_survey = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_type_survey_temp = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_time_interval = new com.google.gson.JsonArray();
		com.google.gson.JsonArray arr_speed = new com.google.gson.JsonArray();
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_type_trial) {
			arr_type_trial.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_time_preparation) {
			arr_time_preparation.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_time_gameplay) {
			arr_time_gameplay.add((dataelementiterator instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_time_interval) {
			arr_time_interval.add((dataelementiterator instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_spawn_opponent) {
			arr_spawn_opponent.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_win) {
			arr_win.add((dataelementiterator instanceof ByteTag _byteTag ? _byteTag.getAsByte() == 1 : false));
		}
		for (Tag dataelementiterator : CacModVariables.MapVariables.get(world).Dat_speed) {
			arr_speed.add((dataelementiterator instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D));
		}
		idx = 0;
		lst_type_survey_temp = new ListTag();
		for (int index0 = 0; index0 < CacModVariables.MapVariables.get(world).Dat_type_survey.size(); index0++) {
			lst_type_survey_temp = ((CacModVariables.MapVariables.get(world).Dat_type_survey.get((int) idx)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).copy();
			for (Tag dataelementiterator : lst_type_survey_temp) {
				arr_type_survey_temp.add((dataelementiterator instanceof IntTag _intTag ? _intTag.getAsInt() : 0));
			}
			arr_type_survey.add(arr_type_survey_temp.deepCopy());
			arr_type_survey_temp = new com.google.gson.JsonArray();
			idx = idx + 1;
		}
		CacMod.LOGGER.info("Dat_time_survey temp");
		CacMod.LOGGER.info("Dat_answer_survey temp");
		subobj_type.add("trial", arr_type_trial);
		obj_session.add("Type", subobj_type);
		subobj_time.add("preparation", arr_time_preparation);
		subobj_time.add("gameplay", arr_time_gameplay);
		subobj_time.add("interval", arr_time_interval);
		obj_session.add("Time", subobj_time);
		subobj_gameplay.add("spawn", arr_spawn_opponent);
		subobj_gameplay.add("win", arr_win);
		subobj_gameplay.add("speed", arr_speed);
		obj_session.add("Gameplay", subobj_gameplay);
		subobj_survey.add("type", arr_type_survey);
		CacMod.LOGGER.info("time");
		CacMod.LOGGER.info("answer");
		obj_session.add("Survey", subobj_survey);
		file_beh = new File(CacModVariables.MapVariables.get(world).Exp_path, File.separator + ("beh_" + CacModVariables.MapVariables.get(world).Exp_subject + ".json"));
		if (!file_beh.exists()) {
			try {
				file_beh.getParentFile().mkdirs();
				file_beh.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			obj_main.add(CacModVariables.MapVariables.get(world).Exp_session, obj_session);
		} else {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file_beh));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj_main = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					obj_main.add(CacModVariables.MapVariables.get(world).Exp_session, obj_session);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		{
			com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
			try {
				FileWriter fileWriter = new FileWriter(file_beh);
				fileWriter.write(mainGSONBuilderVariable.toJson(obj_main));
				fileWriter.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
