package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TutoManageProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String tuto_name = "";
		tuto_name = StringArgumentType.getString(arguments, "name");
		if ((tuto_name).equals("all")) {
			TutoResetProcedure.execute(entity);
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tutorial");
				}
			}
		} else if ((tuto_name).equals("reset")) {
			net.owo.cac.CstTutorial.tuto_id = 0;
		} else if ((tuto_name).equals("beginner")) {
			TutoBeginnerReadyProcedure.execute(world, entity);
		}
	}
}
