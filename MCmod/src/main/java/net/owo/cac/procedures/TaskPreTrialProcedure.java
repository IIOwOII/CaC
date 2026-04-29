package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPreTrialProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Exp_phase = 0;
		CacModVariables.Switch_blank = true;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task");
			}
		}
		FncResetDatPosProcedure.execute();
		FncManageTasktypeProcedure.execute(world);
		FncManageSpawnOpponentProcedure.execute(world);
		FncManageDifficultyProcedure.execute();
		TaskSpawnOpponentProcedure.execute(world);
		if (CacModVariables.Dat_trial_type == 0) {
			EffApplyMorphPredatorProcedure.execute(entity);
		} else if (CacModVariables.Dat_trial_type == 1) {
			EffApplyMorphPreyProcedure.execute(entity);
		}
	}
}
