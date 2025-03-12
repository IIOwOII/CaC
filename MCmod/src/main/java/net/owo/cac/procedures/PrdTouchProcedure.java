package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (!CacModVariables.MapVariables.get(world).TimR_switch) {
			CacModVariables.MapVariables.get(world).Tim_trial_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			TaskPhaseEndProcedure.execute(world);
		} else {
			CacModVariables.MapVariables.get(world).TimR_switch = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("#.##").format(CacModVariables.MapVariables.get(world).Pmt_difficulty))), false);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("####").format(CacModVariables.MapVariables.get(world).TimR_time))), false);
		}
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eTouched!"), false);
		}
	}
}
