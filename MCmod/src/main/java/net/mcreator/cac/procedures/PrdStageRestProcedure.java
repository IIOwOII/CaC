package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.CacMod;

public class PrdStageRestProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Exp_Rest = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		RtnStageRestProcedure.execute(world);
		CacMod.queueServerWork(100, () -> {
			CacModVariables.MapVariables.get(world).Exp_Rest = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			RtnStageRestProcedure.execute(world);
		});
	}
}
