package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TutoManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		String type = "";
		String content = "";
		IniPoolProcedure.execute(world);
		type = StringArgumentType.getString(arguments, "type");
		content = StringArgumentType.getString(arguments, "content");
		if ((type).equals("main")) {
			if ((content).equals("all")) {
				CacMod.LOGGER.info("all");
			}
		} else if ((type).equals("debug")) {
			if ((content).equals("reset")) {
				PrdMeowMoveOffProcedure.execute();
				CacModVariables.MapVariables.get(world).Tuto_switch = false;
				CacModVariables.MapVariables.get(world).syncData(world);
			} else if ((content).equals("moving")) {
				PrdMeowMoveOnProcedure.execute();
				CacModVariables.MapVariables.get(world).Tuto_switch = true;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Tuto_content = content;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}
}
