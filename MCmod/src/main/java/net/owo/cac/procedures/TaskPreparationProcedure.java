package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPreparationProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Exp_phase = 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "worldborder set 8");
			}
		}
		CacModVariables.MapVariables.get(world).Timer_event = "phase_gameplay";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_time = (CacModVariables.MapVariables.get(world).Dat_time_preparation
				.get((int) (CacModVariables.MapVariables.get(world).Exp_trial % CacModVariables.MapVariables.get(world).Dat_time_preparation.size()))) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal("Ready..."), true);
	}
}
