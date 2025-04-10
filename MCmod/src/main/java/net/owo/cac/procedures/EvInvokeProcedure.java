package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class EvInvokeProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7cEvent content is blank!"), false);
		} else if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("session_start")) {
			TaskPreTrialProcedure.execute(world, entity);
		} else if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("phase_gameplay")) {
			CacModVariables.MapVariables.get(world).Switch_AI = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("blank_on_1sec") || (CacModVariables.MapVariables.get(world).Ev_content_curr).equals("blank_on_3sec")) {
			CacModVariables.MapVariables.get(world).Switch_blank = true;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((CacModVariables.MapVariables.get(world).Ev_content_curr).equals("blank_off")) {
			CacModVariables.MapVariables.get(world).Switch_blank = false;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
