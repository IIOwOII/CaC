package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdInitializeOpponentProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("C_Timer", 0);
		CacModVariables.MapVariables.get(world).UUID_opponent = entity.getStringUUID();
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
