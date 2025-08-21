package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModItems;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class PrdItemOptionPrintProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CacModItems.CAC_TEST_ITEM.get()) {
			if (CacModVariables.MapVariables.get(world).Option_tester == 0) {
				CacModVariables.MapVariables.get(world).Option_tester_str = "Increase Difficulty";
				CacModVariables.MapVariables.get(world).syncData(world);
			} else if (CacModVariables.MapVariables.get(world).Option_tester == 1) {
				CacModVariables.MapVariables.get(world).Option_tester_str = "Decrease Difficulty";
				CacModVariables.MapVariables.get(world).syncData(world);
			} else if (CacModVariables.MapVariables.get(world).Option_tester == 2) {
				CacModVariables.MapVariables.get(world).Option_tester_str = "AI Switch";
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_tester_str), true);
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CacModItems.CAC_BUILDER_TOOL.get()) {
			if (CacModVariables.MapVariables.get(world).Option_builder == 0) {
				CacModVariables.MapVariables.get(world).Option_builder_str = "Set 1";
				CacModVariables.MapVariables.get(world).syncData(world);
			} else if (CacModVariables.MapVariables.get(world).Option_builder == 1) {
				CacModVariables.MapVariables.get(world).Option_builder_str = "Set 2";
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(CacModVariables.MapVariables.get(world).Option_builder_str), true);
		}
	}
}
