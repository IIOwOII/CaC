package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.core.BlockPos;

public class TaskSpawnOpponentProcedure {
	public static void execute(LevelAccessor world) {
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double ry = 0;
		double rp = 0;
		ListTag lst_spawnpoint;
		lst_spawnpoint = new ListTag();
		lst_spawnpoint = (CacModVariables.MapVariables.get(world).List_spawnpoint_opponent.get((int) CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		sx = (lst_spawnpoint.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sy = (lst_spawnpoint.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sz = (lst_spawnpoint.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		ry = (lst_spawnpoint.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rp = (lst_spawnpoint.get(4)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		if (CacModVariables.MapVariables.get(world).Dat_trial_type == 0) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_MOUSE.get().spawn(_level, BlockPos.containing(sx, sy, sz), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot((float) ry);
					entityToSpawn.setYBodyRot((float) ry);
					entityToSpawn.setYHeadRot((float) ry);
					entityToSpawn.setXRot((float) rp);
				}
			}
		} else if (CacModVariables.MapVariables.get(world).Dat_trial_type == 1) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_CAT.get().spawn(_level, BlockPos.containing(sx, sy, sz), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot((float) ry);
					entityToSpawn.setYBodyRot((float) ry);
					entityToSpawn.setYHeadRot((float) ry);
					entityToSpawn.setXRot((float) rp);
				}
			}
		}
	}
}
