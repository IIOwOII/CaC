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

public class TutoManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		IniPoolProcedure.execute(world);
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_tutorial));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				if (obj_file.get((StringArgumentType.getString(arguments, "type"))).isJsonArray()) {
					CacModVariables.Tuto_que = obj_file.get((StringArgumentType.getString(arguments, "type"))).getAsJsonArray();
					TutoResetProcedure.execute(world, entity);
					CacModVariables.MapVariables.get(world).Exp_session = "tuto_" + StringArgumentType.getString(arguments, "type");
					CacModVariables.MapVariables.get(world).syncData(world);
					IniLogProcedure.execute(world);
					EvResetProcedure.execute(world);
					TimResetProcedure.execute(world);
					TimCountdownProcedure.execute(world, entity);
					CacModVariables.MapVariables.get(world).Ev_content = "tutorial_start";
					CacModVariables.MapVariables.get(world).syncData(world);
				} else {
					if (!world.isClientSide() && world.getServer() != null)
						world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Please check the command!"), false);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
