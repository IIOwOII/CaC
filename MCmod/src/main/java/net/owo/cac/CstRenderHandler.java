package net.owo.cac;

import net.minecraft.client.gui.GuiGraphics;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import net.owo.cac.CstState;
import net.owo.cac.CstField;
import net.owo.cac.CstAgent;
import net.owo.cac.CstRenderComponent;
import net.owo.cac.network.CacModVariables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstRenderHandler {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
    	// Check if this is the hotbar overlay
		boolean ismeowview = CstState.getMeowView();
        if (ismeowview) {
        	if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()) event.setCanceled(true);
        	if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) event.setCanceled(true);
        }
        
        int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		GuiGraphics gg = event.getGuiGraphics();
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
    	GuiGraphics gg = event.getGuiGraphics();
    	int gw = event.getWindow().getGuiScaledWidth();
		int gh = event.getWindow().getGuiScaledHeight();
    	if (CacModVariables.Switch_blank) CstRenderComponent.renderBlank(gg, gw, gh);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CstState.getMeowView()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
    	if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
    	Minecraft mc = Minecraft.getInstance();
    	MultiBufferSource.BufferSource bs = mc.renderBuffers().bufferSource();
    	
    	PoseStack ps = event.getPoseStack();
    	VertexConsumer vc = bs.getBuffer(RenderType.lines());
    	Vec3 vec_cam = event.getCamera().getPosition();

    	if (CstField.show_field) {
    		Vec3 vec_p_prime = CstAgent.pos_player;
    		Vec3 vec_p = CstAgent.pos_opponent;
    		
			Vec3 field_obstacle = CstField.calFieldObstacle(3, vec_p);
			Vec3 field_wall = CstField.calFieldWall(8, vec_p);
			Vec3 field_player = CstField.calFieldPlayer(12, vec_p.subtract(vec_p_prime));
			Vec3 field_sum = Vec3.ZERO;
			field_sum = field_sum.add(field_obstacle);
			field_sum = field_sum.add(field_wall);
			field_sum = field_sum.add(field_player);
    		
    		Vec3 vec_po = vec_p.add(field_obstacle);
    		Vec3 vec_pw = vec_p.add(field_wall);
    		Vec3 vec_pp = vec_p.add(field_player);
    		Vec3 vec_field = vec_p.add(field_sum);
    		
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_po.subtract(vec_cam), 'x');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_pw.subtract(vec_cam), 'y');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_pp.subtract(vec_cam), 'z');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_field.subtract(vec_cam), 'b');
    	}
    }
}