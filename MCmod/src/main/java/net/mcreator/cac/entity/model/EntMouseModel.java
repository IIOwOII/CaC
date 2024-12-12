package net.mcreator.cac.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.cac.entity.EntMouseEntity;

public class EntMouseModel extends GeoModel<EntMouseEntity> {
	@Override
	public ResourceLocation getAnimationResource(EntMouseEntity entity) {
		return new ResourceLocation("cac", "animations/cac_mouse.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntMouseEntity entity) {
		return new ResourceLocation("cac", "geo/cac_mouse.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntMouseEntity entity) {
		return new ResourceLocation("cac", "textures/entities/" + entity.getTexture() + ".png");
	}

}
