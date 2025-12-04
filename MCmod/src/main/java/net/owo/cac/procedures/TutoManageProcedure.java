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
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		String tuto_type = "";
		IniPoolProcedure.execute(world);
		tuto_type = "tutorial_" + StringArgumentType.getString(arguments, "type");
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
				CacModVariables.Tuto_que = obj_file.get(tuto_type).getAsJsonArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (CacModVariables.Tuto_que.isEmpty()) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Please check the command!"), false);
		} else {
			TutoQueCloneProcedure.execute();
			CacModVariables.Exp_session = tuto_type;
			IniLogProcedure.execute();
			TimResetProcedure.execute(world);
			EvResetProcedure.execute(world);
			CacModVariables.MapVariables.get(world).Ev_content = "tutorial_init";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvQueCallProcedure.execute(world, x, y, z, entity);
		}
	}
}
