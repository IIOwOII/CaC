package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;

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
