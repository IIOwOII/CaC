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
		File log_fitting = new File("");
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_fit = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_cac = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_final = new com.google.gson.JsonObject();
		com.google.gson.JsonObject obj_method = new com.google.gson.JsonObject();
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
		log_fitting = new File((CacModVariables.Dir_behaviors + "/fitting_" + CacModVariables.Exp_session), File.separator + "log_fitting.json");
		FncManageTasktypeProcedure.execute(world);
		if (log_fitting.exists() && !(CacModVariables.Exp_subject).equals("none")) {
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
			obj_final = obj_cac.get("final").getAsJsonObject();
			if (obj_final.get("continuous").isJsonObject()) {
				obj_method = obj_final.get("continuous").getAsJsonObject();
				CacModVariables.Dat_theta = obj_method.get("theta").getAsJsonArray();
				net.owo.cac.CstPsychometric.loadThetaStar();
				CacModVariables.Exp_trial_total = 20;
				if (CacModVariables.Exp_property.contains("S")) {
					net.owo.cac.CstSurvey.initSurvey(false);
				}
				PrdCountdownProcedure.execute();
				TaskSessionStartProcedure.execute(world, entity);
			} else {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Only continuous!"), false);
			}
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eNo fitting file of not registered!\u00A7r"), false);
		}
	}
}
