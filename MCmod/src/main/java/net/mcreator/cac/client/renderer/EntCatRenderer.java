
package net.mcreator.cac.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.OcelotModel;

import net.mcreator.cac.entity.EntCatEntity;

public class EntCatRenderer extends MobRenderer<EntCatEntity, OcelotModel<EntCatEntity>> {
	public EntCatRenderer(EntityRendererProvider.Context context) {
		super(context, new OcelotModel(context.bakeLayer(ModelLayers.OCELOT)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(EntCatEntity entity) {
		return new ResourceLocation("cac:textures/entities/ocelot.png");
	}
}
