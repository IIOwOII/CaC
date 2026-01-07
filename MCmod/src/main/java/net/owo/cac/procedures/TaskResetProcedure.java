package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.Entity;

public class TaskResetProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Switch_AI = false;
		CacModVariables.Switch_blank = false;
		CacModVariables.Switch_timer = false;
		CacModVariables.Switch_debug = false;
		CacModVariables.Switch_trace = false;
		CacModVariables.Switch_scanner = false;
		CacModVariables.TimS_time = 0;
		CacModVariables.TimR_que_time = 0;
		CacModVariables.Ev_occuring = false;
		CacModVariables.Ev_que_loop = false;
		CacModVariables.Ev_que_index = 0;
		CacModVariables.Ev_content = "";
		CacModVariables.Ev_pulse_content = "";
		EffRemoveMorphProcedure.execute(entity);
	}
}
