package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class TutoManageProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		String type = "";
		IniPoolProcedure.execute(world);
		type = StringArgumentType.getString(arguments, "type");
		if ((type).equals("checkpoint")) {
			EvResetProcedure.execute(world);
			TimResetProcedure.execute(world);
			PrdMeowMoveOnProcedure.execute();
			CacModVariables.MapVariables.get(world).Ev_content = "tutorial_checkpoint_start";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvQueCallProcedure.execute(world, x, y, z, entity);
		} else if ((type).equals("debug_racing")) {
			PrdMeowMoveOnProcedure.execute();
			AdpResetProcedure.execute(world, entity);
			CacModVariables.MapVariables.get(world).Tuto_type = 3;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((type).equals("debug_end")) {
			PrdMeowMoveOffProcedure.execute();
			AdpResetProcedure.execute(world, entity);
		}
	}
}
