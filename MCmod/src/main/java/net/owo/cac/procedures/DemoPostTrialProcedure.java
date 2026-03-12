package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class DemoPostTrialProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_phase = 4;
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("####").format(net.owo.cac.CstAgent.getDuration()))), false);
		EffRemoveMorphProcedure.execute(entity);
		TaskPostRunProcedure.execute();
	}
}
