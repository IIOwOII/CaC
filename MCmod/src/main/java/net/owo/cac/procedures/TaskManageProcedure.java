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
import com.mojang.brigadier.arguments.BoolArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_task = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_session = new com.google.gson.JsonArray();
		boolean is_session = false;
		double idx_session = 0;
		CacModVariables.MapVariables.get(world).Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_scanner = BoolArgumentType.getBool(arguments, "scanner");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Switch_scanner) {
			CacModVariables.MapVariables.get(world).Exp_signal = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).TimS_time = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
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
				arr_session = obj_task.get("session").getAsJsonArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		idx_session = 0;
		is_session = false;
		for (int index0 = 0; index0 < (int) arr_session.size(); index0++) {
			if ((CacModVariables.MapVariables.get(world).Exp_session).equals(arr_session.get(((int) idx_session)).getAsString())) {
				is_session = true;
			}
			idx_session = idx_session + 1;
		}
		if (is_session) {
			TaskSessionStartProcedure.execute(world, x, y, z, entity);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Please check the session name!"), false);
		}
	}
}
