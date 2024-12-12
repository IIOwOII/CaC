package net.mcreator.cac.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.cac.network.CacModVariables;
import net.mcreator.cac.init.CacModItems;

public class PrdKeyRightProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CacModItems.CAC_TEST_ITEM.get()) {
			CacModVariables.MapVariables.get(world).Option_tester = CacModVariables.MapVariables.get(world).Option_tester + 1;
			CacModVariables.MapVariables.get(world).syncData(world);
			PrdTestOptionProcedure.execute(world, entity);
		}
	}
}
