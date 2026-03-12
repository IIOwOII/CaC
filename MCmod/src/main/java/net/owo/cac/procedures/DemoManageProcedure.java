package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class DemoManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_subject = "demo";
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task_topview");
			}
		}
		CacModVariables.Dat_trial_type = DoubleArgumentType.getDouble(arguments, "type");
		CacModVariables.Dat_difficulty = DoubleArgumentType.getDouble(arguments, "difficulty");
		CacModVariables.Dat_trial_spawnpoint_opponent = DoubleArgumentType.getDouble(arguments, "spawnpoint");
		if (CacModVariables.Dat_trial_type == 0) {
			CacModVariables.Exp_session = "demo_chasing";
		} else if (CacModVariables.Dat_trial_type == 1) {
			CacModVariables.Exp_session = "demo_chased";
		}
		EvResetProcedure.execute();
		TimResetProcedure.execute();
		IniQueProcedure.execute();
		EvQueCallProcedure.execute(world, entity);
	}
}
