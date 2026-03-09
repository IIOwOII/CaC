package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdTestProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String type = "";
		type = StringArgumentType.getString(arguments, "type");
		if ((type).equals("survey")) {
			net.owo.cac.CstSurvey.startSurvey();
		} else if ((type).equals("surrender")) {
			net.owo.cac.CstSurrender.startSurrender();
		} else if ((type).equals("predator")) {
			EffApplyMorphPredatorProcedure.execute(entity);
			MeowViewOnProcedure.execute();
			MeowMoveOnProcedure.execute();
		} else if ((type).equals("prey")) {
			EffApplyMorphPreyProcedure.execute(entity);
			MeowViewOnProcedure.execute();
			MeowMoveOnProcedure.execute();
		} else if ((type).equals("reset")) {
			EffRemoveMorphProcedure.execute(entity);
			MeowViewOffProcedure.execute();
			MeowMoveOffProcedure.execute();
		}
	}
}
