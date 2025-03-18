package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class TaskManageAddonProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CacModVariables.MapVariables.get(world).Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		CacModVariables.MapVariables.get(world).syncData(world);
		TaskManageProcedure.execute(world, arguments);
	}
}
