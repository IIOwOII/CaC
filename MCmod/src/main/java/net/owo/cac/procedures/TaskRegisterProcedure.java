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
		CacModVariables.Exp_subject = StringArgumentType.getString(arguments, "subject");
		CacModVariables.Exp_mode = StringArgumentType.getString(arguments, "mode");
		if ((CacModVariables.Exp_mode).equals("fmri") || (CacModVariables.Exp_mode).equals("seeg")) {
			CacModVariables.Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + StringArgumentType.getString(arguments, "subject");
			IniInfoTimestampProcedure.execute(world);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eSubject: \u00A7r" + CacModVariables.Exp_subject)), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDirectory: \u00A7r" + CacModVariables.Dir_behaviors)), false);
		} else {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7ePlease check the exp mode! (fmri, seeg)\u00A7r"), false);
		}
	}
}
