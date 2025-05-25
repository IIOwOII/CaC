package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntPseudoMouseEntity;

import net.minecraft.resources.ResourceLocation;

public class EntPseudoMouseModel extends GeoModel<EntPseudoMouseEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntPseudoMouseEntity entity) {
		return new ResourceLocation("cac", "animations/cac_mouse.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntPseudoMouseEntity entity) {
		return new ResourceLocation("cac", "geo/cac_mouse.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntPseudoMouseEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
