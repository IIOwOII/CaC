package net.owo.cac.procedures;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity;

public class CamTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		
		double cam_distance = 5;
		float cam_pitch = 60;
		
		Entity owner = null;
		double offset_x = 0;
		double offset_y = 0;
		double offset_z = 0;
		float owner_yaw = 0;
		double owner_yaw_rad = 0;
		double cam_distance_xz = 0;
		double cam_pitch_rad = Math.toRadians((double) cam_pitch);
		
		if (entity instanceof TamableAnimal _tamEnt ? _tamEnt.isTame() : false) {
			owner = entity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null;
			if (!(owner == null)) {
				cam_distance_xz = Math.cos(cam_pitch_rad) * cam_distance;
				owner_yaw_rad = Math.toRadians((double) owner.getYRot());
				
				offset_x = Math.sin(owner_yaw_rad) * cam_distance_xz;
				offset_z = (-Math.cos(owner_yaw_rad)) * cam_distance_xz;
				offset_y = Math.sin(cam_pitch_rad) * cam_distance;
				
				entity.setPos((offset_x + owner.getX()), (offset_y + owner.getY()), (offset_z + owner.getZ()));
				
				entity.setYRot(owner_yaw);
				entity.setXRot(cam_pitch);
				entity.setYBodyRot(owner_yaw);
				entity.setYHeadRot(owner_yaw);
				entity.yRotO = owner_yaw;
				entity.xRotO = cam_pitch;
			}
		}
	}
}
