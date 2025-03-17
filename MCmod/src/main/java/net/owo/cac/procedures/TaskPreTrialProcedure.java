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
		double trial_type = 0;
		double spawn_opponent = 0;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double ry = 0;
		double rp = 0;
		ListTag lst_spawnpoint;
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task");
			}
		}
		trial_type = FncManageTasktypeProcedure.execute(world);
		spawn_opponent = FncManageSpawnOpponentProcedure.execute(world);
		lst_spawnpoint = new ListTag();
		lst_spawnpoint = (CacModVariables.MapVariables.get(world).Pool_spawn.get((int) spawn_opponent)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		sx = (lst_spawnpoint.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sy = (lst_spawnpoint.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sz = (lst_spawnpoint.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		ry = (lst_spawnpoint.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rp = (lst_spawnpoint.get(4)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		if (trial_type == 0) {
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
		} else if (trial_type == 1) {
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
		if ((CacModVariables.MapVariables.get(world).Exp_session).equals("test_mixed") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chasing") || (CacModVariables.MapVariables.get(world).Exp_session).equals("test_chased")) {
			TaskGameplayProcedure.execute(world, entity);
		} else {
			TaskPreparationProcedure.execute(world, entity);
		}
	}
}
