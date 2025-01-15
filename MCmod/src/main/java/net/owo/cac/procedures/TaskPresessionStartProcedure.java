package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPresessionStartProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		TaskDefaultSettingProcedure.execute(world, entity);
		TaskPresessionSettingProcedure.execute(world);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("title " + "@a " + "title " + "\"\uC7A0\uC2DC \uD6C4 \uC5F0\uC2B5 \uACFC\uC81C\uB97C \uC2DC\uC791\uD569\uB2C8\uB2E4.\""));
			}
		}
		CacModVariables.MapVariables.get(world).Timer_show = "subtitle";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_event = "start_presession";
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_time = 5;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
