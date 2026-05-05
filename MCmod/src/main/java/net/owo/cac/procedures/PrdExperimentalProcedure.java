package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CacMod;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

import java.util.Calendar;

import java.io.File;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world) {
		File hi = new File("");
		hi = new File((FMLPaths.GAMEDIR.get().toString() + "hi/bi"), File.separator + "name.extension");
		if (hi.exists()) {
			CacMod.LOGGER.info(new java.text.SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime()) + "_" + CacModVariables.Exp_subject + "_" + new java.text.DecimalFormat("##").format(0));
		}
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\uD654\uC0B4\uD45C \uCABD\uC73C\uB85C \uC6C0\uC9C1\uC5EC\uC8FC\uC138\uC694!"), false);
	}
}
