package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class PrdTestProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		CacModVariables.MapVariables.get(world).Switch_trace = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_type = DoubleArgumentType.getDouble(arguments, "type");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = DoubleArgumentType.getDouble(arguments, "difficulty_absolute");
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Dat_trial_spawnpoint_opponent = DoubleArgumentType.getDouble(arguments, "spawnpoint_opponent");
		CacModVariables.MapVariables.get(world).syncData(world);
		TaskSpawnOpponentProcedure.execute(world);
		if (CacModVariables.MapVariables.get(world).Dat_trial_type == 0) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_PSEUDO_CAT.get().spawn(_level, BlockPos.containing(0.5, 64, -49.5), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
		} else if (CacModVariables.MapVariables.get(world).Dat_trial_type == 1) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CacModEntities.ENT_PSEUDO_MOUSE.get().spawn(_level, BlockPos.containing(0.5, 64, -49.5), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
		}
		CacModVariables.MapVariables.get(world).Switch_AI = true;
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
