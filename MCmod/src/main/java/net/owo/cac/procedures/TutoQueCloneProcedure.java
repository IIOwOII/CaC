package net.owo.cac.procedures;

import net.owo.cac.CstTutorial;
import net.owo.cac.network.CacModVariables;

public class TutoQueCloneProcedure {
	public static void execute() {
		CstTutorial.tuto_que = CacModVariables.Tuto_que.deepCopy();
	}
}
