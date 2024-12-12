package net.mcreator.cac.procedures;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.cac.network.CacModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class PrdSubjectRegisterProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CacModVariables.MapVariables.get(world).Exp_Subject = StringArgumentType.getString(arguments, "Subject");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Exp_Directory = FMLPaths.GAMEDIR.get().toString() + "/CaC/" + StringArgumentType.getString(arguments, "Subject");
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
