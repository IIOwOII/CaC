package net.owo.cac.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

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
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp tutorial");
				}
			}
		} else if ((tuto_name).equals("reset")) {
			TutoResetProcedure.execute(entity);
		} else if ((tuto_name).equals("beginner")) {
			TutoBeginnerReadyProcedure.execute(entity);
		} else if ((tuto_name).equals("checkpoint")) {
			TutoCheckpointReadyProcedure.execute(entity);
		} else if ((tuto_name).equals("racing")) {
			TutoRacingReadyProcedure.execute(entity);
		} else if ((tuto_name).equals("chasing")) {
			net.owo.cac.CstTutorial.chasingPrepTutorial(entity);
		} else if ((tuto_name).equals("chased")) {
			net.owo.cac.CstTutorial.chasedPrepTutorial(entity);
		} else if ((tuto_name).equals("survey")) {
			net.owo.cac.CstTutorial.surveyTutorial();
		} else if ((tuto_name).equals("surrender")) {
			net.owo.cac.CstTutorial.surrenderTutorial();
		} else if ((tuto_name).equals("book")) {
			net.owo.cac.CstTutorial.tuto_id = 10;
		} else if ((tuto_name).equals("practice")) {
			TutoPracticeReadyProcedure.execute(entity);
		}
	}
}
