package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;

public class IniLogProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Dir_behaviors = FMLPaths.GAMEDIR.get().toString() + "/cacutil/behaviors/" + CacModVariables.MapVariables.get(world).Exp_subject;
		CacModVariables.MapVariables.get(world).syncData(world);
		IniLogEventProcedure.execute(world);
		IniLogPositionProcedure.execute(world);
		IniLogGameplayProcedure.execute(world);
		IniLogSurveyProcedure.execute(world);
		if (CacModVariables.MapVariables.get(world).Switch_scanner) {
			IniLogScannerProcedure.execute(world);
		}
	}
}
