package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class PrdTouchMeowcamProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("timer_time", 0);
		entity.getPersistentData().putDouble("pos_x", (entity.getX()));
		entity.getPersistentData().putDouble("pos_z", (entity.getZ()));
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((x + 500), y, z, CacModVariables.MapVariables.get(world).Pmt_difficulty);
		entity.getPersistentData().putBoolean("timer_switch", true);
	}
}
