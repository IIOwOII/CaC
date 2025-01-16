package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TestManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String name_session = "";
		name_session = StringArgumentType.getString(arguments, "session");
		if ((name_session).equals("chasing")) {
			TestChasingProcedure.execute(world, entity);
		} else if ((name_session).equals("chased")) {
			TestChasedProcedure.execute(world, entity);
		}
	}
}
