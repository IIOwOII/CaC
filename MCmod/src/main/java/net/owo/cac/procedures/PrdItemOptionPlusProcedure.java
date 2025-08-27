package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModItems;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class PrdItemOptionPlusProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double num_option_builder = 0;
		double num_option_tester = 0;
		num_option_tester = 3;
		num_option_builder = 3;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CacModItems.CAC_TEST_ITEM.get()) {
			CacModVariables.MapVariables.get(world).Option_tester = Math.round((CacModVariables.MapVariables.get(world).Option_tester + 1) % num_option_tester);
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CacModItems.CAC_BUILDER_TOOL.get()) {
			CacModVariables.MapVariables.get(world).Option_builder = Math.round((CacModVariables.MapVariables.get(world).Option_builder + 1) % num_option_builder);
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
