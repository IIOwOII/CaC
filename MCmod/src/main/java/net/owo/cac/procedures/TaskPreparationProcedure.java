package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPreparationProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_phase = 1;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "worldborder set 2");
			}
		}
		CacModVariables.Switch_blank = false;
		CacModVariables.Switch_trace = true;
		MeowMoveOnProcedure.execute();
		FncManageTimePreparationProcedure.execute();
		net.owo.cac.CstRenderComponent.renderPatchReserved(10);
	}
}
