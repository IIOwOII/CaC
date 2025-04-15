package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class EvInvokeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String ev_content = "";
		if (CacModVariables.MapVariables.get(world).Ev_occuring) {
			ev_content = CacModVariables.MapVariables.get(world).Ev_content;
			if ((ev_content).equals("")) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Event is blank!"), false);
			} else if ((ev_content).equals("test_start")) {
				CacModVariables.MapVariables.get(world).Switch_que = true;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.Ev_que_loop = true;
				TaskPreRunProcedure.execute(world);
			} else if ((ev_content).equals("phase_pretrial")) {
				TaskPreTrialProcedure.execute(world, entity);
			} else if ((ev_content).equals("phase_preparation")) {
				TaskPreparationProcedure.execute(world, entity);
			} else if ((ev_content).equals("phase_gameplay")) {
				TaskGameplayProcedure.execute(world, entity);
			} else if ((ev_content).equals("phase_gameplay_end")) {
				TaskGameplayEndProcedure.execute(world, x, y, z);
			} else if ((ev_content).equals("phase_survey")) {
				TaskSurveyProcedure.execute(world, x, y, z, entity);
			} else if ((ev_content).equals("phase_interval")) {
				TaskIntervalProcedure.execute(world);
			} else if ((ev_content).equals("phase_posttrial")) {
				TaskPostTrialProcedure.execute(world);
			} else if ((ev_content).equals("test_end")) {
				CacModVariables.MapVariables.get(world).Switch_que = false;
				CacModVariables.MapVariables.get(world).syncData(world);
				TaskSessionEndProcedure.execute(world);
			}
			if (CacModVariables.Ev_que_loop) {
				CacModVariables.MapVariables.get(world).Ev_content = CacModVariables.Ev_que.get(((int) CacModVariables.Ev_que_index)).getAsString();
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.Ev_que_index = (CacModVariables.Ev_que_index + 1) % CacModVariables.Ev_que.size();
			}
			CacModVariables.MapVariables.get(world).Ev_occuring = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
