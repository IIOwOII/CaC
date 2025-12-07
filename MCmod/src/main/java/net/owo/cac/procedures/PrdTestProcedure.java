package net.owo.cac.procedures;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdTestProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		String type = "";
		type = StringArgumentType.getString(arguments, "type");
		if ((type).equals("survey")) {
			net.owo.cac.CstSurvey.startSurvey();
		} else if ((type).equals("surrender")) {
			net.owo.cac.CstSurrender.startSurrender();
		}
	}
}
