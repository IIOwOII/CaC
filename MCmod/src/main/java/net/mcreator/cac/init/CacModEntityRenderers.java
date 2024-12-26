
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.cac.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.cac.client.renderer.EntPlayerMouseRenderer;
import net.mcreator.cac.client.renderer.EntPlayerCatRenderer;
import net.mcreator.cac.client.renderer.EntMouseRenderer;
import net.mcreator.cac.client.renderer.EntMeowCamRenderer;
import net.mcreator.cac.client.renderer.EntCatRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CacModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CacModEntities.ENT_CAT.get(), EntCatRenderer::new);
		event.registerEntityRenderer(CacModEntities.ENT_PLAYER_CAT.get(), EntPlayerCatRenderer::new);
		event.registerEntityRenderer(CacModEntities.ENT_MOUSE.get(), EntMouseRenderer::new);
		event.registerEntityRenderer(CacModEntities.ENT_PLAYER_MOUSE.get(), EntPlayerMouseRenderer::new);
		event.registerEntityRenderer(CacModEntities.ENT_MEOW_CAM.get(), EntMeowCamRenderer::new);
	}
}
