package net.mcreator.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.cac.entity.EntMeowCamEntity;

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
