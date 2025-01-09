package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.ListTag;

public class TaskDefaultSettingProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Switch_Task = false;
		CacModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof ServerPlayer _player)
			_player.setGameMode(GameType.ADVENTURE);
		entity.setInvisible(true);
		CacModVariables.MapVariables.get(world).Timer_random_preparation = new ListTag();
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
