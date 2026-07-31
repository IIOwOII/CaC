package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TaskRegisterProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		boolean available_mode = false;
		boolean available_group = false;
		CacModVariables.Exp_subject = StringArgumentType.getString(arguments, "subject");
		CacModVariables.Exp_mode = StringArgumentType.getString(arguments, "mode");
		CacModVariables.Exp_group = StringArgumentType.getString(arguments, "group");
		available_mode = (CacModVariables.Exp_mode).equals("fmri") || (CacModVariables.Exp_mode).equals("seeg") || (CacModVariables.Exp_mode).equals("beh");
		available_group = (CacModVariables.Exp_group).equals("A") || (CacModVariables.Exp_group).equals("B");
		if (available_mode && available_group) {
			CacModVariables.Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + StringArgumentType.getString(arguments, "subject");
			IniInfoTimestampProcedure.execute(world);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eSubject: \u00A7r" + CacModVariables.Exp_subject)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDirectory: \u00A7r" + CacModVariables.Dir_behaviors)), false);
		} else {
			if (!available_mode) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7ePlease check the exp mode! (fmri, seeg, beh)\u00A7r"), false);
			}
			if (!available_group) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7ePlease check the group! (A, B)\u00A7r"), false);
			}
		}
	}
}
