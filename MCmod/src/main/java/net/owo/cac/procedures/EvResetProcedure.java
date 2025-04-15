package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;

public class EvResetProcedure {
	public static void execute(LevelAccessor world) {
		CacModVariables.MapVariables.get(world).Switch_que = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_occuring = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.Ev_que_loop = false;
		CacModVariables.Ev_que_index = 0;
		CacModVariables.MapVariables.get(world).Ev_content = "";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_pulse_content = "";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
