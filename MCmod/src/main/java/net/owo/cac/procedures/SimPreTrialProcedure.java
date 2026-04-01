package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class SimPreTrialProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.Exp_phase = 0;
		FncResetDatPosProcedure.execute();
		FncManageTasktypeProcedure.execute(world);
		FncManageSpawnOpponentProcedure.execute(world);
		FncManageDifficultyProcedure.execute();
		TaskSpawnOpponentProcedure.execute(world);
		if (CacModVariables.Dat_trial_type == 0) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_PSEUDO_CAT.get().spawn(_level, BlockPos.containing(0.5, 64, -49.5), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
		} else if (CacModVariables.Dat_trial_type == 1) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_PSEUDO_MOUSE.get().spawn(_level, BlockPos.containing(0.5, 64, -49.5), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
		}
	}
}
