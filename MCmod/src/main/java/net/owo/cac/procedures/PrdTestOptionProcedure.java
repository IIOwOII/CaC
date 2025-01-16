package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class PrdTestOptionProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double num_option = 0;
		num_option = 2;
		if (CacModVariables.MapVariables.get(world).Option_tester < 0 || CacModVariables.MapVariables.get(world).Option_tester >= num_option) {
			CacModVariables.MapVariables.get(world).Option_tester = CacModVariables.MapVariables.get(world).Option_tester % num_option;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (CacModVariables.MapVariables.get(world).Option_tester == 0) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Increase Difficulty";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_tester == 1) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Decrease Difficulty";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_tester_str), true);
	}
}
