package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class UdtEventStartProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String event = "";
		event = CacModVariables.MapVariables.get(world).Timer_event;
		CacModVariables.MapVariables.get(world).Timer_event = "none";
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((event).equals("map_show")) {
			TaskMapShowProcedure.execute(world, entity);
		} else if ((event).equals("map_return")) {
			TaskMapReturnProcedure.execute(entity);
		} else if ((event).equals("phase_preparation")) {
			TaskPreparationProcedure.execute(world, entity);
		} else if ((event).equals("phase_gameplay")) {
			TaskGameplayProcedure.execute(world, entity);
		} else if ((event).equals("phase_survey")) {
			TaskSurveyProcedure.execute(world, x, y, z, entity);
		} else if ((event).equals("phase_interval")) {
			TaskIntervalProcedure.execute(world);
		} else if ((event).equals("start_test") || (event).equals("start_presession")) {
			TaskPreparationProcedure.execute(world, entity);
		} else if ((event).equals("end_session")) {
			TaskSessionEndProcedure.execute(world);
		}
	}
}
