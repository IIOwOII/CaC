package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
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
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "worldborder set 8");
			}
		}
		FncManageTimePreparationProcedure.execute(world);
		CacModVariables.MapVariables.get(world).Ev_que_waittime = CacModVariables.MapVariables.get(world).Dat_time_preparation;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_que_content = "phase_preparation";
		CacModVariables.MapVariables.get(world).syncData(world);
		EvQueStartProcedure.execute(world);
	}
}
