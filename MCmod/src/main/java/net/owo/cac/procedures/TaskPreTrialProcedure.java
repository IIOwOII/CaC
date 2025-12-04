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
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task");
			}
		}
		CacModVariables.Dat_pos_time = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_x = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_z = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_r = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_time_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_player_r_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_x_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_z_prep = new com.google.gson.JsonArray();
		CacModVariables.Dat_pos_opponent_r_prep = new com.google.gson.JsonArray();
		FncManageTasktypeProcedure.execute(world);
		FncManageSpawnOpponentProcedure.execute(world);
		FncManageDifficultyProcedure.execute(world);
		TaskSpawnOpponentProcedure.execute(world);
		if (CacModVariables.Dat_trial_type == 0) {
			EffApplyMorphPredatorProcedure.execute(entity);
		} else if (CacModVariables.Dat_trial_type == 1) {
			EffApplyMorphPreyProcedure.execute(entity);
		}
	}
}
