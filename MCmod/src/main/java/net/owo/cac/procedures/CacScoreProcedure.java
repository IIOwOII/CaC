package net.owo.cac.procedures;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class CacScoreProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		boolean param_switch = false;
		boolean param_positive = false;
		param_switch = BoolArgumentType.getBool(arguments, "switch");
		param_positive = BoolArgumentType.getBool(arguments, "positive");
		net.owo.cac.CstPsychometric.score_switch = param_switch;
		net.owo.cac.CstPsychometric.score_positive = param_positive;
	}
}
