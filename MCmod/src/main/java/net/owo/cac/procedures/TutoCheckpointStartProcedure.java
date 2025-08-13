package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class TutoCheckpointStartProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.MapVariables.get(world).Tuto_type = 2;
		CacModVariables.MapVariables.get(world).syncData(world);
		{
			Entity _ent = entity;
			Scoreboard _sc = _ent.level().getScoreboard();
			Objective _so = _sc.getObjective("tutorial_checkpoint");
			if (_so == null)
				_so = _sc.addObjective("tutorial_checkpoint", ObjectiveCriteria.DUMMY, Component.literal("tutorial_checkpoint"), ObjectiveCriteria.RenderType.INTEGER);
			_sc.getOrCreatePlayerScore(_ent.getScoreboardName(), _so).setScore(2000);
		}
		CacModVariables.MapVariables.get(world).Tuto_checkpoint_index = 0;
		CacModVariables.MapVariables.get(world).syncData(world);
		CacModVariables.MapVariables.get(world).Ev_content = "tutorial_checkpoint";
		CacModVariables.MapVariables.get(world).syncData(world);
	}
}
