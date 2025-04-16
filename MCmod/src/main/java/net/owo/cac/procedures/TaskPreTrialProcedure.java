package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPreTrialProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double ry = 0;
		double rp = 0;
		ListTag lst_spawnpoint;
		CacModVariables.MapVariables.get(world).Exp_phase = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
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
		FncManageTasktypeProcedure.execute(world);
		FncManageSpawnOpponentProcedure.execute(world);
		FncManageDifficultyProcedure.execute(world);
		lst_spawnpoint = new ListTag();
		lst_spawnpoint = (CacModVariables.MapVariables.get(world).List_spawnpoint_opponent.get((int) CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		sx = (lst_spawnpoint.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sy = (lst_spawnpoint.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sz = (lst_spawnpoint.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		ry = (lst_spawnpoint.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rp = (lst_spawnpoint.get(4)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		if (CacModVariables.MapVariables.get(world).Dat_trial_type == 0) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_MOUSE.get().spawn(_level, BlockPos.containing(sx, sy, sz), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot((float) ry);
					entityToSpawn.setYBodyRot((float) ry);
					entityToSpawn.setYHeadRot((float) ry);
					entityToSpawn.setXRot((float) rp);
				}
			}
			EffApplyMorphPredatorProcedure.execute(entity);
		} else if (CacModVariables.MapVariables.get(world).Dat_trial_type == 1) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_CAT.get().spawn(_level, BlockPos.containing(sx, sy, sz), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot((float) ry);
					entityToSpawn.setYBodyRot((float) ry);
					entityToSpawn.setYHeadRot((float) ry);
					entityToSpawn.setXRot((float) rp);
				}
			}
			EffApplyMorphPreyProcedure.execute(entity);
		}
	}
}
