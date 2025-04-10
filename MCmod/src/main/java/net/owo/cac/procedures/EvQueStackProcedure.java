package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;

public class EvQueStackProcedure {
	public static void execute(LevelAccessor world) {
		double session_type = 0;
		String session_name = "";
		session_name = CacModVariables.MapVariables.get(world).Exp_session;
		if ((session_name).equals("")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Message"), false);
		} else if ((session_name).equals("test_mixed") || (session_name).equals("test_chasing") || (session_name).equals("test_chased")) {
			session_type = 0;
		} else if ((session_name).equals("chasing") || (session_name).equals("chased")) {
			session_type = 1;
		}
		if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("session_start")) {
			CacModVariables.MapVariables.get(world).Ev_content_next = "phase_preparation";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		CacModVariables.MapVariables.get(world).Switch_que = true;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
