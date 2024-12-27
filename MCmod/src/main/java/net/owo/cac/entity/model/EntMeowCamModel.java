package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntMeowCamEntity;

import net.minecraft.resources.ResourceLocation;

public class EntMeowCamModel extends GeoModel<EntMeowCamEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntMeowCamEntity entity) {
		return new ResourceLocation("cac", "animations/cac_meowcam.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntMeowCamEntity entity) {
		return new ResourceLocation("cac", "geo/cac_meowcam.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntMeowCamEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
