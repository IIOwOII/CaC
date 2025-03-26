package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class PrdTouchProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_pulse_content = "touch";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvPulseRecordProcedure.execute(world);
		EvQueImmediateProcedure.execute(world);
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eTouched!"), false);
		}
	}
}
