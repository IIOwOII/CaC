package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.cac.network.CacModVariables;

public class UdtEventStartProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String event = "";
		event = CacModVariables.MapVariables.get(world).Timer_event;
		CacModVariables.MapVariables.get(world).Timer_event = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((event).equals("pre_AP")) {
			TaskPresessionShowmapProcedure.execute(world, entity);
		} else if ((event).equals("pre_return")) {
			TaskPresessionReturnProcedure.execute(entity);
		}
	}
}
