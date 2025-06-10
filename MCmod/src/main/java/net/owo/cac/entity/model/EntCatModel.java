package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntCatEntity;

import net.minecraft.resources.ResourceLocation;

public class EntCatModel extends GeoModel<EntCatEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntCatEntity entity) {
		return new ResourceLocation("cac", "animations/cac_cat.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntCatEntity entity) {
		return new ResourceLocation("cac", "geo/cac_cat.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntCatEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
