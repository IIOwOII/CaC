package net.owo.cac.procedures;

import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String name_session = "";
		name_session = StringArgumentType.getString(arguments, "session");
		if ((name_session).equals("introduction")) {
			CacMod.LOGGER.info("not yet");
		} else if ((name_session).equals("presession")) {
			TaskPresessionStartProcedure.execute(world, entity);
		}
	}
}
