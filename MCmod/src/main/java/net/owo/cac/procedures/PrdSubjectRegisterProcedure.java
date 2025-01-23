package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdSubjectRegisterProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CacModVariables.MapVariables.get(world).Exp_subject = StringArgumentType.getString(arguments, "subject");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_path = FMLPaths.GAMEDIR.get().toString() + "/cacutil/" + StringArgumentType.getString(arguments, "subject");
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
