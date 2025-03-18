package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class EvEnvokeProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String ev_content = "";
		ev_content = CacModVariables.MapVariables.get(world).Tim_event_content;
		if ((ev_content).equals("")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Event content is blank!"), false);
		} else if ((ev_content).equals("phase_preparation")) {
			TaskGameplayProcedure.execute(world, entity);
		}
		CacModVariables.MapVariables.get(world).Tim_event_duration = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Tim_event_content = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
