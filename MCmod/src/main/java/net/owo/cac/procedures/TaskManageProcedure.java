package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		boolean is_task = false;
		CacModVariables.Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
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
				is_task = obj_task.get(CacModVariables.Exp_session).isJsonPrimitive() ? obj_task.get(CacModVariables.Exp_session).getAsJsonPrimitive().isString() : false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (is_task && !(CacModVariables.Exp_subject).equals("none")) {
			if (CacModVariables.Exp_property.contains("S")) {
				net.owo.cac.CstSurvey.initSurvey(false);
			}
			TaskSessionStartProcedure.execute(world, entity);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eUnavailable task name or not registered!\u00A7r"), false);
		}
	}
}
