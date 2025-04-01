package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String session = "";
		CacModVariables.MapVariables.get(world).Exp_session = StringArgumentType.getString(arguments, "session");
		CacModVariables.MapVariables.get(world).syncData(world);
		session = StringArgumentType.getString(arguments, "session");
		if ((session).equals("test_mixed") || (session).equals("test_chasing") || (session).equals("test_chased")) {
			TaskPreRunProcedure.execute(world, x, y, z, entity);
		} else if ((session).equals("introduction")) {
			CacMod.LOGGER.info("not yet");
		} else if ((session).equals("presession")) {
			CacMod.LOGGER.info("not yet");
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Please check the name of session!"), false);
		}
	}
}
