package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class TestManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String session_demo = "";
		String session = "";
		session_demo = StringArgumentType.getString(arguments, "session_demo");
		if ((session_demo).equals("chasing")) {
			TestChasingProcedure.execute(world, entity);
		} else if ((session_demo).equals("chased")) {
			TestChasedProcedure.execute(world, entity);
		} else {
			session = StringArgumentType.getString(arguments, "session");
			CacModVariables.MapVariables.get(world).Dat_trial_total = DoubleArgumentType.getDouble(arguments, "trials");
			CacModVariables.MapVariables.get(world).syncData(world);
			if ((session).equals("mixed")) {
				TestMixedStartProcedure.execute(world, entity);
			}
		}
	}
}
