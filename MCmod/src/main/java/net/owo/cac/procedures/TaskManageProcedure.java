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
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		String exp_property = "";
		CacModVariables.Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		IniPoolProcedure.execute(world);
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
				exp_property = obj_task.get(CacModVariables.Exp_session).getAsString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (exp_property.contains("C")) {
			CacModVariables.MapVariables.get(world).Switch_scanner = true;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.Exp_signal = false;
			CacModVariables.MapVariables.get(world).TimS_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Switch_scanner = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		TaskSessionStartProcedure.execute(world, x, y, z, entity);
	}
}
