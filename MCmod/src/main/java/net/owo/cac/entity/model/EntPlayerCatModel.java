package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntPlayerCatEntity;

import net.minecraft.resources.ResourceLocation;

public class EntPlayerCatModel extends GeoModel<EntPlayerCatEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntPlayerCatEntity entity) {
		return new ResourceLocation("cac", "animations/cac_cat.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntPlayerCatEntity entity) {
		return new ResourceLocation("cac", "geo/cac_cat.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntPlayerCatEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
