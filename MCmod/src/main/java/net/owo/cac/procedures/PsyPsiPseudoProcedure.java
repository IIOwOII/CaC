package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;

public class PsyPsiPseudoProcedure {
	public static void execute() {
		CacModVariables.Dat_psy_param = new ListTag();
		CacModVariables.Dat_psy_param.addTag(0, DoubleTag.valueOf(0.96));
		CacModVariables.Dat_psy_param.addTag(1, DoubleTag.valueOf(0.24));
		CacModVariables.Dat_psy_param.addTag(2, DoubleTag.valueOf(0.04));
		CacModVariables.Dat_psy_param.addTag(3, DoubleTag.valueOf(0.04));
	}
}
