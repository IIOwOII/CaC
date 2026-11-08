package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

public class FncManageTimePreparationProcedure {
	public static void execute() {
		CacModVariables.Dat_time_preparation = 100;
		CacModVariables.TimR_que_time = CacModVariables.TimR_time + CacModVariables.Dat_time_preparation * 20;
	}
}
