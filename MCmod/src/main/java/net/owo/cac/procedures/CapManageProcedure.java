package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
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
		CacModVariables.Exp_session = "fitting";
		CacModVariables.Psy_task = StringArgumentType.getString(arguments, "task");
		CacModVariables.Psy_method = StringArgumentType.getString(arguments, "method");
		CacModVariables.Psy_function = StringArgumentType.getString(arguments, "function");
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
		if ((CacModVariables.Psy_method).equals("both")) {
			net.owo.cac.CstPsychometric.method_type = 0;
		} else if ((CacModVariables.Psy_method).equals("binary")) {
			net.owo.cac.CstPsychometric.method_type = 1;
		} else if ((CacModVariables.Psy_method).equals("continuous")) {
			net.owo.cac.CstPsychometric.method_type = 2;
		}
		if ((CacModVariables.Psy_function).equals("default")) {
			net.owo.cac.CstPsychometric.func_type = 0;
		}
		if ((CacModVariables.Psy_task).equals("debug")) {
			net.owo.cac.CstPsychometric.initPsy();
			net.owo.cac.CstPsychometric.updateTrialBefore();
			net.owo.cac.CstPsychometric.debugValue();
		} else if ((CacModVariables.Psy_task).equals("chasing")) {
			net.owo.cac.CstPsychometric.trial_type = 0;
			net.owo.cac.CstPsychometric.task_type = 0;
			net.owo.cac.CstPsychometric.initPsy();
			PrdCountdownProcedure.execute();
			TaskSessionStartProcedure.execute(world, entity);
		} else if ((CacModVariables.Psy_task).equals("chased")) {
			net.owo.cac.CstPsychometric.trial_type = 1;
			net.owo.cac.CstPsychometric.task_type = 1;
			net.owo.cac.CstPsychometric.initPsy();
			PrdCountdownProcedure.execute();
			TaskSessionStartProcedure.execute(world, entity);
		} else if ((CacModVariables.Psy_task).equals("both")) {
			net.owo.cac.CstPsychometric.task_type = 2;
			net.owo.cac.CstPsychometric.initPsy();
			PrdCountdownProcedure.execute();
			TaskSessionStartProcedure.execute(world, entity);
		}
	}
}
