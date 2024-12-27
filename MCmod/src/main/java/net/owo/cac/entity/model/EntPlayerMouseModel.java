package net.owo.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.owo.cac.entity.EntPlayerMouseEntity;

import net.minecraft.resources.ResourceLocation;

public class EntPlayerMouseModel extends GeoModel<EntPlayerMouseEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntPlayerMouseEntity entity) {
		return new ResourceLocation("cac", "animations/cac_mouse.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntPlayerMouseEntity entity) {
		return new ResourceLocation("cac", "geo/cac_mouse.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntPlayerMouseEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
