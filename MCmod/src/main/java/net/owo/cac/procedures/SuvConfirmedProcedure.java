package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.IntTag;

public class SuvConfirmedProcedure {
	public static void execute(LevelAccessor world) {
		double ord = 0;
		CacModVariables.MapVariables.get(world).Exp_phase = 3.7;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_survey = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_time = CacModVariables.MapVariables.get(world).TimR_time - CacModVariables.MapVariables.get(world).SuvT_time;
		CacModVariables.MapVariables.get(world).syncData(world);
		ord = (CacModVariables.MapVariables.get(world).Dat_survey_order.get((int) CacModVariables.MapVariables.get(world).SuvT_index)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		CacModVariables.MapVariables.get(world).Dat_survey_time.setTag((int) ord, IntTag.valueOf((int) CacModVariables.MapVariables.get(world).SuvT_time));
		CacModVariables.MapVariables.get(world).Dat_survey_answer.setTag((int) ord, IntTag.valueOf((int) CacModVariables.MapVariables.get(world).SuvT_value));
		CacModVariables.MapVariables.get(world).SuvT_index = CacModVariables.MapVariables.get(world).SuvT_index + 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (CacModVariables.MapVariables.get(world).SuvT_index < CacModVariables.MapVariables.get(world).Suv_reference.size()) {
			CacModVariables.MapVariables.get(world).Ev_content = "survey_waiting";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Ev_content = "phase_surrender";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
