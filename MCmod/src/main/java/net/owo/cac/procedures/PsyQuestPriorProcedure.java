package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.nbt.DoubleTag;

public class PsyQuestPriorProcedure {
	public static void execute() {
		double Ia = 0;
		double Ib = 0;
		double Ic = 0;
		double Id = 0;
		double Ga = 0;
		double Gb = 0;
		double Gc = 0;
		double Gd = 0;
		double sa = 0;
		double sb = 0;
		double sc = 0;
		double sd = 0;
		double Da = 0;
		double Db = 0;
		double Dc = 0;
		double Dd = 0;
		Ia = CacModVariables.Psy_quest_param_prior.get(0).getAsDouble();
		Ib = CacModVariables.Psy_quest_param_prior.get(1).getAsDouble();
		Ic = CacModVariables.Psy_quest_param_prior.get(2).getAsDouble();
		Id = CacModVariables.Psy_quest_param_prior.get(3).getAsDouble();
		Ga = CacModVariables.Psy_quest_param_shape.get(0).getAsDouble();
		Gb = CacModVariables.Psy_quest_param_shape.get(1).getAsDouble();
		Gc = CacModVariables.Psy_quest_param_shape.get(2).getAsDouble();
		Gd = CacModVariables.Psy_quest_param_shape.get(3).getAsDouble();
		sa = 0;
		for (int index0 = 0; index0 < (int) Ga; index0++) {
			Da = Math.pow((sa - Ia) / Ga, 2);
			sb = 0;
			for (int index1 = 0; index1 < (int) Gb; index1++) {
				Db = Math.pow((sb - Ib) / Gb, 2);
				sc = 0;
				for (int index2 = 0; index2 < (int) Gc; index2++) {
					Dc = Math.pow((sc - Ic) / Gc, 2);
					sd = 0;
					for (int index3 = 0; index3 < (int) Gd; index3++) {
						Dd = Math.pow((sd - Id) / Gd, 2);
						CacModVariables.Psy_quest_L.setTag((int) net.owo.cac.CstPsychometric.flattenIndex(sa, sb, sc, sd), DoubleTag.valueOf(Math.log(net.owo.cac.CstPsychometric.funcPrior(Da, Db, Dc, Dd))));
						sd = sd + 1;
					}
					sc = sc + 1;
				}
				sb = sb + 1;
			}
			sa = sa + 1;
		}
	}
}
