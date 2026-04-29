package net.owo.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdUpdateProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		String target = "";
		target = StringArgumentType.getString(arguments, "target");
		if ((target).equals("scan")) {
			net.owo.cac.CstField.scanObstacle(world);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eScan Obstacle!\u00A7r"), false);
		} else if ((target).equals("info")) {
			net.owo.cac.CstField.recObstacle();
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eRecord Obstacle!\u00A7r"), false);
		}
	}
}
