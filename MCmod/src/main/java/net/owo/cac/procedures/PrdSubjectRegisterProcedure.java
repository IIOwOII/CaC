package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdSubjectRegisterProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		File file = new File("");
		com.google.gson.JsonObject obj_main = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_sub = new com.google.gson.JsonObject();
		double num_obj = 0;
		String str_obj = "";
		CacModVariables.MapVariables.get(world).Exp_subject = StringArgumentType.getString(arguments, "subject");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_path = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.MapVariables.get(world).Exp_subject;
		CacModVariables.MapVariables.get(world).syncData(world);
		file = new File(CacModVariables.MapVariables.get(world).Exp_path, File.separator + "ini_time.json");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			str_obj = "0th";
		} else {
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
					num_obj = obj_main.size();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (num_obj == 1) {
				str_obj = "1st";
			} else if (num_obj == 2) {
				str_obj = "2nd";
			} else if (num_obj == 3) {
				str_obj = "3rd";
			} else {
				str_obj = new java.text.DecimalFormat("#").format(num_obj) + "th";
			}
		}
		obj_sub.addProperty("time", Calendar.getInstance().getTime().toString());
		obj_main.add(str_obj, obj_sub);
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
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esubject: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_subject)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7epath: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_path)), false);
		}
	}
}
