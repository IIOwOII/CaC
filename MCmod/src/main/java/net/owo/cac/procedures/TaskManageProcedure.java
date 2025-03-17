package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CacModVariables.MapVariables.get(world).Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("introduction")) {
			CacMod.LOGGER.info("not yet");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("not yet");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed")) {
			CacMod.LOGGER.info("not yet");
		}
	}
}
