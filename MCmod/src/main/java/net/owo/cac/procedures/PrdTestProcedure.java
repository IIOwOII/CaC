package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdTestProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String type = "";
		type = StringArgumentType.getString(arguments, "type");
		if ((type).equals("survey")) {
			net.owo.cac.CstSurvey.initSurvey(false);
			net.owo.cac.CstSurvey.startSurvey();
		} else if ((type).equals("surrender")) {
			net.owo.cac.CstSurrender.startSurrender();
		} else if ((type).equals("move")) {
			MeowViewOnProcedure.execute();
			MeowMoveOnProcedure.execute();
		} else if ((type).equals("ai")) {
			if (CacModVariables.Switch_AI) {
				CacModVariables.Switch_AI = false;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("AI Off"), true);
			} else {
				CacModVariables.Switch_AI = true;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("AI On"), true);
				CacModVariables.Dat_difficulty = 1;
				net.owo.cac.CstAgent.setDuration(600);
			}
		} else if ((type).equals("path")) {
			if (net.owo.cac.CstAgent.show_path) {
				net.owo.cac.CstAgent.show_path = false;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("hide path"), true);
			} else {
				net.owo.cac.CstAgent.show_path = true;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("show path"), true);
			}
		} else if ((type).equals("field")) {
			if (net.owo.cac.CstField.show_field) {
				net.owo.cac.CstField.show_field = false;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("hide field"), true);
			} else {
				net.owo.cac.CstField.show_field = true;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("show field"), true);
			}
		} else if ((type).equals("obstacle")) {
			if (net.owo.cac.CstField.show_obstacle) {
				net.owo.cac.CstField.show_obstacle = false;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("hide obstacle"), true);
			} else {
				net.owo.cac.CstField.show_obstacle = true;
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("show obstacle"), true);
			}
		} else if ((type).equals("predator")) {
			EffApplyMorphPredatorProcedure.execute(entity);
			MeowViewOnProcedure.execute();
			MeowMoveOnProcedure.execute();
		} else if ((type).equals("prey")) {
			EffApplyMorphPreyProcedure.execute(entity);
			MeowViewOnProcedure.execute();
			MeowMoveOnProcedure.execute();
		} else if ((type).equals("reset")) {
			EffRemoveMorphProcedure.execute(entity);
			MeowViewOffProcedure.execute();
			MeowMoveOffProcedure.execute();
		}
	}
}
