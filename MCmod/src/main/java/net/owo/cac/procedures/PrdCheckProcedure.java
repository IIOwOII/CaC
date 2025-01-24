package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

import net.owo.cac.CstState;

public class PrdCheckProcedure {
	public static void execute(LevelAccessor world) {
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7esubject: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_subject)), false);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7epath: \u00A7r" + CacModVariables.MapVariables.get(world).Exp_path)), false);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eobstacles: \u00A7r" + new java.text.DecimalFormat("##").format(CacModVariables.MapVariables.get(world).List_obstacle.size()))), false);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7ewalls: \u00A7r" + new java.text.DecimalFormat("##").format(CacModVariables.MapVariables.get(world).List_wall.size()))), false);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7espawns: \u00A7r" + new java.text.DecimalFormat("##").format(CacModVariables.MapVariables.get(world).Pool_spawn.size()))), false);
		if (!CstState.getMeowview()) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7cNot meowview!"), false);
		}
	}
}
