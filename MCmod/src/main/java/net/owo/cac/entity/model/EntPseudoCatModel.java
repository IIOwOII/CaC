package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntPseudoCatEntity;

import net.minecraft.resources.ResourceLocation;

public class EntPseudoCatModel extends GeoModel<EntPseudoCatEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntPseudoCatEntity entity) {
		return new ResourceLocation("cac", "animations/cac_cat.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntPseudoCatEntity entity) {
		return new ResourceLocation("cac", "geo/cac_cat.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntPseudoCatEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
