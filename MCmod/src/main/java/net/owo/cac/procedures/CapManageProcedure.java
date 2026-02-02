package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CapManageProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		CacModVariables.Psy_method = StringArgumentType.getString(arguments, "method");
	}
}
