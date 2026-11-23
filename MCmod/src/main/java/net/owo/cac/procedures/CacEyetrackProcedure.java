package net.owo.cac.procedures;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class CacEyetrackProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		if (BoolArgumentType.getBool(arguments, "switch")) {
			net.owo.cac.CstEyetrack.setSwitchEyetrack(true);
		} else {
			net.owo.cac.CstEyetrack.setSwitchEyetrack(false);
		}
	}
}
