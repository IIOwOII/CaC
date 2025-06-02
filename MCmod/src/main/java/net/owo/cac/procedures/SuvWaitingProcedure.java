package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

public class SuvWaitingProcedure {
	public static void execute(LevelAccessor world) {
		double ord_survey = 0;
		CacModVariables.MapVariables.get(world).Exp_phase = 3.1;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_content = "survey_progress";
		CacModVariables.MapVariables.get(world).syncData(world);
		ord_survey = (CacModVariables.MapVariables.get(world).Dat_survey_order.get((int) CacModVariables.MapVariables.get(world).Dat_survey_index)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		CacModVariables.MapVariables.get(world).Dat_survey_name = (CacModVariables.MapVariables.get(world).List_survey_name.get((int) ord_survey)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_type = (CacModVariables.MapVariables.get(world).List_survey_type.get((int) ord_survey)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_value = (CacModVariables.MapVariables.get(world).List_survey_initial.get((int) ord_survey)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_survey_value_pre = (CacModVariables.MapVariables.get(world).Dat_survey_answer_pre.get((int) ord_survey)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables
				.get(world).Dat_survey_range_lower = (((CacModVariables.MapVariables.get(world).List_survey_range.get((int) ord_survey)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).get(0)) instanceof IntTag _intTag
						? _intTag.getAsInt()
						: 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables
				.get(world).Dat_survey_range_upper = (((CacModVariables.MapVariables.get(world).List_survey_range.get((int) ord_survey)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).get(1)) instanceof IntTag _intTag
						? _intTag.getAsInt()
						: 0;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
