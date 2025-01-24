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
		String session = "";
		session = StringArgumentType.getString(arguments, "session");
		if ((session).equals("chasing")) {
			TestChasingProcedure.execute(world, entity);
		} else if ((session).equals("chased")) {
			TestChasedProcedure.execute(world, entity);
		} else {
			CacModVariables.MapVariables.get(world).Dat_trial_total = DoubleArgumentType.getDouble(arguments, "trials");
			CacModVariables.MapVariables.get(world).syncData(world);
			if ((session).equals("mixed")) {
				TestMixedStartProcedure.execute(world, entity);
			}
		}
	}
}
