package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.cac.network.CacModVariables;

public class AiMouseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 field_obstacle = Vec3.ZERO;
		Vec3 field_player = Vec3.ZERO;
		Vec3 field_sum = Vec3.ZERO;
		Vec3 field_wall = Vec3.ZERO;
		if (CacModVariables.MapVariables.get(world).Switch_Task) {
			if (!entity.getPersistentData().getBoolean("C_Touch")) {
				field_obstacle = FncFieldObstacleProcedure.execute(world, x, y, z).scale((entity.getPersistentData().getDouble("k_obstacle")));
				field_wall = FncFieldWallProcedure.execute(world, x, y, z).scale((entity.getPersistentData().getDouble("k_wall")));
				field_player = FncFieldPlayerProcedure.execute(world, x, z).scale((entity.getPersistentData().getDouble("k_opponent")));
				field_sum = (field_obstacle.add(field_wall)).add(field_player);
				if (entity instanceof Mob _entity)
					_entity.getNavigation().moveTo((x + field_sum.x()), y, (z + field_sum.z()), CacModVariables.MapVariables.get(world).Pmt_difficulty);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((new java.text.DecimalFormat("##.##").format(field_sum.x()) + "" + new java.text.DecimalFormat("##.##").format(field_sum.z()))), false);
			}
		}
	}
}
