package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

public class FncManageSpawnProcedure {
	public static ListTag execute(LevelAccessor world) {
		double idx_spawn = 0;
		ListTag pos_spawn;
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test")) {
			idx_spawn = CacModVariables.MapVariables.get(world).Exp_trial % 4;
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("presession")) {
			CacMod.LOGGER.info("temp");
		} else if ((CacModVariables.MapVariables.get(world).Exp_session).equals("chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("chased")) {
			CacMod.LOGGER.info("temp");
		} else {
			CacModVariables.MapVariables.get(world).Log_error = "invalid_session";
			CacModVariables.MapVariables.get(world).syncData(world);
			CacErrorProcedure.execute(world);
		}
		pos_spawn = new ListTag();
		CacModVariables.MapVariables.get(world).Dat_spawn_opponent.addTag((int) CacModVariables.MapVariables.get(world).Exp_trial, IntTag.valueOf((int) idx_spawn));
		pos_spawn = (CacModVariables.MapVariables.get(world).Pool_spawn.get((int) idx_spawn)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		return pos_spawn;
	}
}
