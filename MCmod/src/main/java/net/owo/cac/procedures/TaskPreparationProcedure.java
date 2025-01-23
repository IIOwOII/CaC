package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TaskPreparationProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		ListTag pos_spawn;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double ry = 0;
		double rp = 0;
		double type_trial = 0;
		CacModVariables.MapVariables.get(world).Exp_phase = 1;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_AI = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Switch_blank = false;
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
		type_trial = FncManageTasktypeProcedure.execute(world);
		pos_spawn = FncManageSpawnProcedure.execute(world);
		FncManageSpeedProcedure.execute(world);
		sx = (pos_spawn.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sy = (pos_spawn.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sz = (pos_spawn.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		ry = (pos_spawn.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rp = (pos_spawn.get(4)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		if (type_trial == 0) {
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
		} else if (type_trial == 1) {
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
		CacModVariables.MapVariables.get(world).Timer_time = FncManageTimepoolPreparationProcedure.execute(world);
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Timer_event = "phase_gameplay";
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal("Ready..."), true);
	}
}
