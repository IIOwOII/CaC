package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double distance_player = 0;
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			if (entity.getPersistentData().getDouble("C_Timer") <= 0) {
				entity.getPersistentData().putDouble("C_Timer", 0.5);
				if (entity instanceof Mob _entity)
					_entity.getNavigation().moveTo((CacModVariables.MapVariables.get(world).Pos_player.x()), (CacModVariables.MapVariables.get(world).Pos_player.y()), (CacModVariables.MapVariables.get(world).Pos_player.z()),
							(0.565685424949238 * Math.pow(CacModVariables.MapVariables.get(world).Pmt_difficulty, 0.5)));
			}
			entity.getPersistentData().putDouble("C_Timer", (entity.getPersistentData().getDouble("C_Timer") - 0.05));
		}
	}
}
