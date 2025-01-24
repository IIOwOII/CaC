package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class AiOpponentMoveProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 field_obstacle = Vec3.ZERO;
		Vec3 field_player = Vec3.ZERO;
		Vec3 field_sum = Vec3.ZERO;
		Vec3 field_wall = Vec3.ZERO;
		field_player = FncFieldPlayerProcedure.execute(world, x, z, entity);
		field_obstacle = FncFieldObstacleProcedure.execute(world, x, z, entity);
		field_wall = FncFieldWallProcedure.execute(world, x, z, entity);
		field_sum = field_player.add((field_obstacle.add(field_wall)));
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((x + field_sum.x()), y, (z + field_sum.z()), CacModVariables.MapVariables.get(world).Pmt_difficulty);
		if (CacModVariables.MapVariables.get(world).Switch_debug) {
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList()
						.broadcastSystemMessage(Component.literal(("\u00A7efield: \u00A7r(" + new java.text.DecimalFormat("##.#").format(field_sum.x()) + "," + new java.text.DecimalFormat("##.#").format(field_sum.z()) + ")")), false);
		}
	}
}
