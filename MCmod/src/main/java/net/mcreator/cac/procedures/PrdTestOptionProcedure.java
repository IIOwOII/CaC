package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.cac.network.CacModVariables;

public class PrdTestOptionProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Option_tester == 0) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Reset";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_tester == 1) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Increase Difficulty";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_tester == 2) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Decrease Difficulty";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_tester == 3) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Increase Distance Scale";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_tester == 4) {
			CacModVariables.MapVariables.get(world).Option_tester_str = "Decrease Distance Scale";
			CacModVariables.MapVariables.get(world).syncData(world);
		} else {
			CacModVariables.MapVariables.get(world).Option_tester = 0;
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Option_tester_str = "Reset";
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_tester_str), true);
	}
}
