package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject obj_file = new com.google.gson.JsonObject();
		com.google.gson.JsonArray arr_que = new com.google.gson.JsonArray();
		CacModVariables.MapVariables.get(world).Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed")) {
			TaskPreRunProcedure.execute(world, entity);
		}
	}
}
