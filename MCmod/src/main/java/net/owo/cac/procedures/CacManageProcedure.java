package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CacManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String psy_type = "";
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_fit = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_psy = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_final = new com.google.gson.JsonObject();
		File log_fitting = new File("");
		CacModVariables.Exp_session = StringArgumentType.getString(arguments, "task");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_task));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_task = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				CacModVariables.Exp_property = obj_task.get(CacModVariables.Exp_session).getAsString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CacModVariables.Psy_task = StringArgumentType.getString(arguments, "task");
		CacModVariables.Psy_method = StringArgumentType.getString(arguments, "method");
		CacModVariables.Psy_function = StringArgumentType.getString(arguments, "function");
		psy_type = CacModVariables.Psy_task + "_" + CacModVariables.Psy_method + "_" + CacModVariables.Psy_function;
		log_fitting = new File((CacModVariables.Dir_behaviors + "/fitting"), File.separator + "log_fitting.json");
		if (log_fitting.exists()) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(log_fitting));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					obj_fit = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					obj_cac = obj_fit.get("cac").getAsJsonObject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			obj_psy = obj_cac.get(psy_type).getAsJsonObject();
			obj_final = obj_psy.get("final").getAsJsonObject();
			CacModVariables.Psy_bin_param_best = obj_final.get("param_best").getAsJsonArray();
			PrdCountdownProcedure.execute();
			TaskSessionStartProcedure.execute(world, entity);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("No fitting file!"), false);
		}
	}
}
