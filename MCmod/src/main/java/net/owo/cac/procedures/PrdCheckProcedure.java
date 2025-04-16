package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdCheckProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		String target = "";
		target = StringArgumentType.getString(arguments, "target");
		if ((target).equals("experiment")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esubject: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_subject)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esession: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_session)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esignal: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_signal)), false);
		}
		if ((target).equals("switch")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eAI: \u00A7r" + CacModVariables.MapVariables.get(world).Switch_AI)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eblank: \u00A7r" + CacModVariables.MapVariables.get(world).Switch_blank)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eque: \u00A7r" + CacModVariables.MapVariables.get(world).Switch_que)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7etimer: \u00A7r" + CacModVariables.MapVariables.get(world).Switch_timer)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7etrace: \u00A7r" + CacModVariables.MapVariables.get(world).Switch_trace)), false);
		}
		if ((target).equals("event")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7econtent: \u00A7r" + CacModVariables.MapVariables.get(world).Ev_content)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eque: \u00A7r" + CacModVariables.Ev_que)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eque index: \u00A7r" + CacModVariables.Ev_que_index)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eloop: \u00A7r" + CacModVariables.Ev_que_loop)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eoccuring: \u00A7r" + CacModVariables.MapVariables.get(world).Ev_occuring)), false);
		}
	}
}
