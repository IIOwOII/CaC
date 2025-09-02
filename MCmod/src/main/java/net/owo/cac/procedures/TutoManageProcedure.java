package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TutoManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		IniPoolProcedure.execute(world);
		if ((StringArgumentType.getString(arguments, "type")).equals("main")) {
			PrdMeowMoveOnProcedure.execute();
			CacModVariables.MapVariables.get(world).Tuto_switch = true;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tuto_progress = "moving";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((StringArgumentType.getString(arguments, "type")).equals("checkpoint")) {
			EvResetProcedure.execute(world);
			TimResetProcedure.execute(world);
			PrdMeowMoveOnProcedure.execute();
			CacModVariables.MapVariables.get(world).Ev_content = "tutorial_checkpoint_start";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvQueCallProcedure.execute(world, x, y, z, entity);
		} else if ((StringArgumentType.getString(arguments, "type")).startsWith("debug")) {
			PrdMeowMoveOnProcedure.execute();
			AdpResetProcedure.execute(world, entity);
			if ((StringArgumentType.getString(arguments, "type")).equals("debug_checkpoint")) {
				CacModVariables.MapVariables.get(world).Tuto_score = 2000;
				CacModVariables.MapVariables.get(world).syncData(world);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tuto_checkpoint");
					}
				}
			} else if ((StringArgumentType.getString(arguments, "type")).equals("debug_racing")) {
				CacModVariables.MapVariables.get(world).Tuto_score = 2000;
				CacModVariables.MapVariables.get(world).syncData(world);
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tuto_racing");
					}
				}
			}
			CacModVariables.MapVariables.get(world).Tuto_score_running = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((StringArgumentType.getString(arguments, "type")).equals("reset")) {
			PrdMeowMoveOffProcedure.execute();
			AdpResetProcedure.execute(world, entity);
			CacModVariables.MapVariables.get(world).Tuto_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Tuto_progress = "";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
