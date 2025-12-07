package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

import java.io.File;

public class IniPoolProcedure {
	public static void execute(LevelAccessor world) {
		String dir_components = "";
		dir_components = FMLPaths.GAMEDIR.get().toString() + "/cacutil/components";
		CacModVariables.Dir_components = dir_components;
		CacModVariables.Pool_event = new File(dir_components, File.separator + "pool_event.json");
		CacModVariables.Pool_task = new File(dir_components, File.separator + "pool_task.json");
		CacModVariables.Pool_point = new File(dir_components, File.separator + "pool_point.json");
		CacModVariables.Pool_que = new File(dir_components, File.separator + "pool_que.json");
		CacModVariables.Pool_psychometric = new File(dir_components, File.separator + "pool_psychometric.json");
		CacModVariables.Pool_random = new File(dir_components, File.separator + "pool_random.json");
		IniPoolPointProcedure.execute(world);
		IniPoolPsychometricProcedure.execute();
		IniPoolRandomProcedure.execute(world);
		if (CacModVariables.Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\u00A7eDirectory(components): \u00A7r" + dir_components)), false);
		}
	}
}
