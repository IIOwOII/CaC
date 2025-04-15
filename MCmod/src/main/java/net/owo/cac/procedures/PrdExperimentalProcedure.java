package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrdExperimentalProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		CacModVariables.Dat_pos_time.add(CacModVariables.MapVariables.get(world).TimR_time);
		CacModVariables.Dat_pos_player_x.add(((entity.position()).x()));
		CacModVariables.Dat_pos_player_z.add(((entity.position()).z()));
		CacModVariables.Dat_pos_player_r.add((entity.getYRot()));
	}
}
