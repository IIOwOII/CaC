package net.owo.cac.mixins;

import net.minecraft.core.BlockPos;
import net.owo.cac.CstState;

import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MxnMeowviewMixin {
	@Unique private static final double CAM_DISTANCE = 5;
	@Unique private static final float CAM_PITCH = 60;
	@Unique private static final double CAM_PITCH_RAD = CAM_PITCH * 0.017453292519943295;
	@Unique private static final double CAM_DISTANCE_XZ= Math.cos(CAM_PITCH_RAD) * CAM_DISTANCE;

	@Shadow private Entity entity;
	@Shadow private Vec3 position;
	@Shadow @Final private BlockPos.MutableBlockPos blockPosition;

	@Shadow private float xRot;
	@Shadow private float yRot;
	@Shadow @Final private Quaternionf rotation;
	@Shadow @Final private Vector3f forwards;
	@Shadow @Final private Vector3f up;
	@Shadow @Final private Vector3f left;

	@Inject(at = @At("TAIL"), method = "setPosition(Lnet/minecraft/world/phys/Vec3;)V")
	private void setPositionMixin(CallbackInfo ci) {
		if (CstState.getMeowview()) {
			double yaw = this.yRot * 0.017453292519943295;
			Vec3 addedVec = new Vec3(Math.sin(yaw) * CAM_DISTANCE_XZ, Math.sin(CAM_PITCH_RAD) * CAM_DISTANCE, -Math.cos(yaw) * CAM_DISTANCE_XZ);
			this.position = this.position.add(addedVec);
			this.blockPosition.set(this.position.x, this.position.y, this.position.z);
		}
	}

	@Inject(at = @At("TAIL"), method = "setRotation")
	private void setRotationMixin(CallbackInfo ci) {
		if (CstState.getMeowview()) {
			this.xRot = CAM_PITCH;
			this.rotation.rotationYXZ(-this.yRot * 0.017453292F, CAM_PITCH * 0.017453292F, 0.0F);
			this.forwards.set(0.0F, 0.0F, 1.0F).rotate(this.rotation);
			this.up.set(0.0F, 1.0F, 0.0F).rotate(this.rotation);
			this.left.set(1.0F, 0.0F, 0.0F).rotate(this.rotation);
		}
	}
}