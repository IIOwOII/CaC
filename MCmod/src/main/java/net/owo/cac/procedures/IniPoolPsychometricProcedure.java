package net.owo.cac.procedures;

public class IniPoolPsychometricProcedure {
	public static void execute() {
		PsyBinInitializeProcedure.execute();
		PsyConInitializeProcedure.execute();
		PsyNeoInitializeProcedure.execute();
	}
}
