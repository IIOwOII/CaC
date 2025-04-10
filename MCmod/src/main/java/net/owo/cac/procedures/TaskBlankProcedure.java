package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class TaskBlankProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!CacModVariables.MapVariables.get(world).Switch_blank) {
			if (CacModVariables.MapVariables.get(world).Exp_phase == 2) {
				CacModVariables.MapVariables.get(world).Ev_que_waittime = 3;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			CacModVariables.MapVariables.get(world).Ev_que_content = "blank_on";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvQueStartProcedure.execute(world);
		} else {
			if (CacModVariables.MapVariables.get(world).Exp_phase == 2) {
				CacModVariables.MapVariables.get(world).Ev_que_waittime = 3;
				CacModVariables.MapVariables.get(world).syncData(world);
				EffRemoveMorphProcedure.execute(entity);
			}
			CacModVariables.MapVariables.get(world).Ev_que_content = "blank_off";
			CacModVariables.MapVariables.get(world).syncData(world);
			EvQueStartProcedure.execute(world);
		}
	}
}
