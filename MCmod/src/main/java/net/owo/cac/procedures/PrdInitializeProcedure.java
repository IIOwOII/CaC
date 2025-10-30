package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class PrdInitializeProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		IniPoolProcedure.execute(world);
		CacModVariables.MapVariables.get(world).List_obstacle = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_obstacle = FncScanObstacleProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).List_wall = FncScanWallProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "title @p times 10 100 10");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "bossbar add timebar \"time\"");
			}
		}
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("\u00A7eInitialized!"), false);
	}
}
