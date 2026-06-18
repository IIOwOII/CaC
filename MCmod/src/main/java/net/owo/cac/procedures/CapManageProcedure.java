package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstPsychometric;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CapManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		boolean is_available = false;
		CacModVariables.Exp_session = "fitting_" + StringArgumentType.getString(arguments, "task");
		CacModVariables.Psy_task = StringArgumentType.getString(arguments, "task");
		CacModVariables.Psy_method = StringArgumentType.getString(arguments, "method");
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
				is_available = obj_task.get(CacModVariables.Exp_session).isJsonPrimitive() ? obj_task.get(CacModVariables.Exp_session).getAsJsonPrimitive().isString() : false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if ((CacModVariables.Exp_mode).equals("seeg")) {
			CstPsychometric.adjustIG(0.08);
		} else {
			CstPsychometric.adjustIG(0.05);
		}
		if ((CacModVariables.Psy_method).equals(".")) {
			CacModVariables.Psy_method = "continuous";
		}
		if ((CacModVariables.Psy_method).equals("both")) {
			net.owo.cac.CstPsychometric.method_type = 0;
		} else if ((CacModVariables.Psy_method).equals("binary")) {
			net.owo.cac.CstPsychometric.method_type = 1;
		} else if ((CacModVariables.Psy_method).equals("continuous")) {
			net.owo.cac.CstPsychometric.method_type = 2;
		} else {
			is_available = false;
		}
		if ((CacModVariables.Psy_task).equals("interleaved")) {
			net.owo.cac.CstPsychometric.task_type = 2;
			net.owo.cac.CstPsychometric.initPsy();
		} else if ((CacModVariables.Psy_task).equals("chasing")) {
			net.owo.cac.CstPsychometric.task_type = 0;
			net.owo.cac.CstPsychometric.initPsy();
		} else if ((CacModVariables.Psy_task).equals("chased")) {
			net.owo.cac.CstPsychometric.task_type = 1;
			net.owo.cac.CstPsychometric.initPsy();
		} else {
			is_available = false;
		}
		if (is_available && !(CacModVariables.Exp_subject).equals("none")) {
			PrdCountdownProcedure.execute();
			TaskSessionStartProcedure.execute(world, entity);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eUnavailable task name or not registered!\u00A7r"), false);
		}
	}
}
