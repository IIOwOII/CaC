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

public class TutoChasingReadyProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double ry = 0;
		double rp = 0;
		ListTag lst_spawnpoint;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "cac_tp task");
			}
		}
		lst_spawnpoint = new ListTag();
		lst_spawnpoint = (CacModVariables.MapVariables.get(world).List_spawnpoint_opponent.get(0)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		sx = (lst_spawnpoint.get(0)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sy = (lst_spawnpoint.get(1)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		sz = (lst_spawnpoint.get(2)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		ry = (lst_spawnpoint.get(3)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
		rp = (lst_spawnpoint.get(4)) instanceof DoubleTag _doubleTag ? _doubleTag.getAsDouble() : 0.0D;
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
		CacModVariables.Dat_difficulty_absolute = 0.9;
		CacModVariables.Switch_AI = false;
		MeowViewOnProcedure.execute();
		MeowMoveOffProcedure.execute();
		CacModVariables.TimC_time = 100;
		CacModVariables.TimC_que = "tutorial_chasing";
		CacModVariables.TimC_switch = true;
	}
}
