package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class SimManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_subject = "simulation";
		CacModVariables.Dat_trial_type = DoubleArgumentType.getDouble(arguments, "type");
		CacModVariables.Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		if (DoubleArgumentType.getDouble(arguments, "difficulty") == 0) {
			CacModVariables.Dat_difficulty_absolute = 0.8;
			CacModVariables.Dat_difficulty_relative = 0;
		} else {
			CacModVariables.Dat_difficulty_absolute = DoubleArgumentType.getDouble(arguments, "difficulty");
			CacModVariables.Dat_difficulty_relative = DoubleArgumentType.getDouble(arguments, "difficulty");
		}
		if (CacModVariables.Dat_trial_type == 0) {
			CacModVariables.Exp_session = "simulation_chasing";
		} else if (CacModVariables.Dat_trial_type == 1) {
			CacModVariables.Exp_session = "simulation_chased";
		}
		IniPoolProcedure.execute(world);
		EvResetProcedure.execute();
		TimResetProcedure.execute();
		IniLogProcedure.execute();
		IniQueProcedure.execute();
		EvQueCallProcedure.execute(world, x, y, z, entity);
	}
}
