package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class IniLogProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.MapVariables.get(world).Exp_subject;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDirectory(behaviors): \u00A7r" + CacModVariables.MapVariables.get(world).Dir_behaviors)), false);
		IniLogEventProcedure.execute(world);
		IniLogPositionProcedure.execute(world);
		IniLogTrialresultProcedure.execute(world);
	}
}
