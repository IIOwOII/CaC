package net.owo.cac.procedures;

import net.owo.cac.init.CacModMobEffects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskCleanMobProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.isPassenger()) {
			entity.stopRiding();
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @e[team=Player]");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @e[team=Opponent]");
			}
		}
		if (entity instanceof LivingEntity _livEnt4 && _livEnt4.hasEffect(CacModMobEffects.EFF_MORPH_PREDATOR.get())) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(CacModMobEffects.EFF_MORPH_PREDATOR.get());
		}
		if (entity instanceof LivingEntity _livEnt6 && _livEnt6.hasEffect(CacModMobEffects.EFF_MORPH_PREY.get())) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(CacModMobEffects.EFF_MORPH_PREY.get());
		}
	}
}
