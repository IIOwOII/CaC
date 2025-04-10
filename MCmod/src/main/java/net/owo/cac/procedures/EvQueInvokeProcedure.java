package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class EvQueInvokeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String que_content = "";
		EvQueEndProcedure.execute(world);
		que_content = CacModVariables.MapVariables.get(world).Ev_que_content;
		CacModVariables.MapVariables.get(world).Ev_que_waittime = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_que_content = "";
		CacModVariables.MapVariables.get(world).syncData(world);
		if ((que_content).equals("")) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Event content is blank!"), false);
		} else if ((que_content).equals("phase_preparation")) {
			TaskInterphaseProcedure.execute(world, x, y, z, entity);
		} else if ((que_content).equals("phase_gameplay")) {
			CacModVariables.MapVariables.get(world).Switch_AI = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			FncManageTimeGameplayProcedure.execute(world);
			TaskBlankProcedure.execute(world, entity);
		} else if ((que_content).equals("blank_on")) {
			CacModVariables.MapVariables.get(world).Switch_blank = true;
			CacModVariables.MapVariables.get(world).syncData(world);
			TaskBlankProcedure.execute(world, entity);
		} else if ((que_content).equals("blank_off")) {
			CacModVariables.MapVariables.get(world).Switch_blank = false;
			CacModVariables.MapVariables.get(world).syncData(world);
			TaskInterphaseProcedure.execute(world, x, y, z, entity);
		}
	}
}
