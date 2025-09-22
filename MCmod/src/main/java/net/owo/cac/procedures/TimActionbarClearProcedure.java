package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class TimActionbarClearProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Msg_actionbar_text = "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Msg_actionbar_switch = false;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
