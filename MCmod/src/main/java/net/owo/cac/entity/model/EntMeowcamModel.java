package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntMeowcamEntity;

import net.minecraft.resources.ResourceLocation;

public class EntMeowcamModel extends GeoModel<EntMeowcamEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntMeowcamEntity entity) {
		return new ResourceLocation("cac", "animations/cac_meowcam.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntMeowcamEntity entity) {
		return new ResourceLocation("cac", "geo/cac_meowcam.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntMeowcamEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
