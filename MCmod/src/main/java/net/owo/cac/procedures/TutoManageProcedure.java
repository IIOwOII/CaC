package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TutoManageProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String tuto_name = "";
		tuto_name = StringArgumentType.getString(arguments, "name");
		if ((tuto_name).equals("all")) {
			TutoResetProcedure.execute(entity);
			CacModVariables.Tuto_index = 0;
			CacMod.LOGGER.info("temp");
		} else if ((tuto_name).equals("beginner")) {
			PrdMeowMoveOffProcedure.execute();
			CacModVariables.Tuto_index = 1;
		}
	}
}
