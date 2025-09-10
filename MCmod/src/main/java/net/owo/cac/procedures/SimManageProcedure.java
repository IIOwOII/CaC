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
		CacModVariables.MapVariables.get(world).Exp_subject = "simulation";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_type = DoubleArgumentType.getDouble(arguments, "type");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_trial_total = DoubleArgumentType.getDouble(arguments, "trial");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = DoubleArgumentType.getDouble(arguments, "difficulty");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_relative = DoubleArgumentType.getDouble(arguments, "difficulty");
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).Dat_trial_type == 0) {
			CacModVariables.MapVariables.get(world).Exp_session = "simulation_chasing";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Dat_trial_type == 1) {
			CacModVariables.MapVariables.get(world).Exp_session = "simulation_chased";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		IniPoolProcedure.execute(world);
		EvResetProcedure.execute(world);
		TimResetProcedure.execute(world);
		IniLogProcedure.execute(world);
		IniQueProcedure.execute(world);
		EvQueCallProcedure.execute(world, x, y, z, entity);
	}
}
