package net.mcreator.cac.procedures;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

public class CamTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		Entity owner = null;
		double offset_x = 0;
		double offset_y = 0;
		double offset_z = 0;
		double owner_yaw = 0;
		double cam_distance = 0;
		double cam_pitch = 0;
		double cam_distance_xz = 0;
		if (entity instanceof TamableAnimal _tamEnt ? _tamEnt.isTame() : false) {
			owner = entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null;
			if (!(owner == null)) {
				cam_pitch = Math.toRadians(60);
				cam_distance = 5;
				cam_distance_xz = Math.cos(cam_pitch) * cam_distance;
				owner_yaw = Math.toRadians(owner.getYRot());
				offset_x = Math.sin(owner_yaw) * cam_distance_xz;
				offset_z = (-Math.cos(owner_yaw)) * cam_distance_xz;
				offset_y = Math.sin(cam_pitch) * cam_distance;
				{
					Entity _ent = entity;
					_ent.teleportTo((offset_x + owner.getX()), (offset_y + owner.getY()), (offset_z + owner.getZ()));
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport((offset_x + owner.getX()), (offset_y + owner.getY()), (offset_z + owner.getZ()), _ent.getYRot(), _ent.getXRot());
				}
				{
					Entity _ent = entity;
					_ent.setYRot((float) Math.toDegrees(owner_yaw));
					_ent.setXRot((float) Math.toDegrees(cam_pitch));
					_ent.setYBodyRot(_ent.getYRot());
					_ent.setYHeadRot(_ent.getYRot());
					_ent.yRotO = _ent.getYRot();
					_ent.xRotO = _ent.getXRot();
					if (_ent instanceof LivingEntity _entity) {
						_entity.yBodyRotO = _entity.getYRot();
						_entity.yHeadRotO = _entity.getYRot();
					}
				}
			}
		}
	}
}
