package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdDespawnPlayerProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).UUID_player = "";
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			Entity _entityTeam = entity;
			PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam("Player");
			if (_pt != null)
				_entityTeam.level().getScoreboard().removePlayerFromTeam(_entityTeam.getStringUUID(), _pt);
		}
	}
}
