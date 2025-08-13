package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class IniPoolProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dir_components = FMLPaths.GAMEDIR.get().toString() + "/cacutil/components";
		CacModVariables.MapVariables.get(world).syncData(world);
		IniPoolTaskProcedure.execute(world);
		IniPoolEventProcedure.execute(world);
		IniPoolPointProcedure.execute(world);
		IniPoolQueProcedure.execute(world);
		IniPoolSurveyProcedure.execute(world);
		IniPoolPsychometricProcedure.execute(world);
		IniPoolScriptProcedure.execute(world);
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDirectory(components): \u00A7r" + CacModVariables.MapVariables.get(world).Dir_components)), false);
		}
	}
}
