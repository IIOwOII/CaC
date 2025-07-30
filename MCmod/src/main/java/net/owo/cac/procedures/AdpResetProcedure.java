package net.owo.cac.procedures;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class AdpResetProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			Scoreboard _sc = _ent.level().getScoreboard();
			Objective _so = _sc.getObjective("adv_moving");
			if (_so == null)
				_so = _sc.addObjective("adv_moving", ObjectiveCriteria.DUMMY, Component.literal("adv_moving"), ObjectiveCriteria.RenderType.INTEGER);
			_sc.getOrCreatePlayerScore(_ent.getScoreboardName(), _so).setScore(0);
		}
	}
}
