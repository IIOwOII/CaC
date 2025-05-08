package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class AiCatProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Switch_AI) {
			if (CacModVariables.MapVariables.get(world).Time_AI == 0 && !world.isClientSide()) {
				if (entity instanceof Mob _entity)
					_entity.getNavigation().moveTo((CacModVariables.MapVariables.get(world).Pos_player.x()), (CacModVariables.MapVariables.get(world).Pos_player.y()), (CacModVariables.MapVariables.get(world).Pos_player.z()),
							(0.565685424949238 * Math.pow(CacModVariables.MapVariables.get(world).Dat_difficulty_absolute, 0.5)));
			}
		}
	}
}
