package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.DoubleTag;

public class SuvWaitingProcedure {
	public static void execute(LevelAccessor world) {
		double ord = 0;
		CacModVariables.MapVariables.get(world).Exp_phase = 3.3;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = true;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_content = "survey_progress";
		CacModVariables.MapVariables.get(world).syncData(world);
		ord = (CacModVariables.MapVariables.get(world).Dat_survey_order.get((int) CacModVariables.MapVariables.get(world).SuvT_index)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
		CacModVariables.MapVariables.get(world).SuvT_name = (CacModVariables.MapVariables.get(world).Suv_reference.get((int) ord)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_type = (CacModVariables.MapVariables.get(world).Suv_type.get((int) ord)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_range_lower = (CacModVariables.MapVariables.get(world).Suv_range_lower.get((int) ord)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_range_upper = (CacModVariables.MapVariables.get(world).Suv_range_upper.get((int) ord)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_label_low = (CacModVariables.MapVariables.get(world).Suv_label_low.get((int) ord)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_label_mid = (CacModVariables.MapVariables.get(world).Suv_label_mid.get((int) ord)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_label_high = (CacModVariables.MapVariables.get(world).Suv_label_high.get((int) ord)) instanceof StringTag _stringTag ? _stringTag.getAsString() : "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_value = (CacModVariables.MapVariables.get(world).Suv_initial.get((int) ord)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).SuvT_value_pre = (CacModVariables.MapVariables.get(world).SuvT_answer_pre.get((int) ord)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
