package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Calendar;

import java.io.File;

public class PrdExperimentalProcedure {
	public static void execute() {
		File hi = new File("");
		hi = new File((FMLPaths.GAMEDIR.get().toString() + "hi/bi"), File.separator + "name.extension");
		if (hi.exists()) {
			CacMod.LOGGER.info(new java.text.SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime()) + "_" + CacModVariables.Exp_subject + "_" + new java.text.DecimalFormat("##").format(0));
		}
	}
}
