package net.owo.cac.procedures;

import net.owo.cac.entity.EntMeowcamEntity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;
import java.util.Comparator;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class PrdMeowControlProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments) {
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
			for (Entity entityiterator : _entfound) {
				if (entityiterator instanceof EntMeowcamEntity) {
					if (!world.isClientSide()) {
						if (entityiterator instanceof Mob _entity)
							_entity.getNavigation().moveTo((entityiterator.getX() + DoubleArgumentType.getDouble(arguments, "meow_dx")), (entityiterator.getY()), (entityiterator.getZ() + DoubleArgumentType.getDouble(arguments, "meow_dz")),
									(0.565685424949238 * Math.pow(DoubleArgumentType.getDouble(arguments, "meow_speed"), 0.5)));
					}
				}
			}
		}
	}
}
