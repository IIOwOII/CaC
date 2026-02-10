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
		if ((CacModVariables.Psy_method).equals("binary")) {
			if ((CacModVariables.Psy_function).equals("logistic")) {
				net.owo.cac.CstPsychometric.initBin(0);
				PrdCountdownProcedure.execute();
				TaskSessionStartProcedure.execute(world, entity);
			}
		}
	}
}
