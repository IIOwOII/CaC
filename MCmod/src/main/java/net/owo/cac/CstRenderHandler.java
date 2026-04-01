package net.owo.cac;

import java.util.ArrayList;

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
import net.owo.cac.CstReplay;
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
	public static ArrayList<Vec3> vec_nodes = new ArrayList<>();
	
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
    public static void onRenderGuiPose(RenderGuiEvent.Post event) {
    	if (CstReplay.isRecording()) CstReplay.readFrame();
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
		
		if (CstAgent.ent_predator == null || CstAgent.ent_prey == null) return;
		if (CstField.show_field) {
    		Vec3 vec_p_prime = CstAgent.ent_predator.position();
    		Vec3 vec_p = CstAgent.ent_prey.position();
    		
			Vec3 field_obstacle = CstField.calFieldObstacle(3, vec_p);
			Vec3 field_wall = CstField.calFieldWall(8, vec_p);
			Vec3 field_predator = CstField.calFieldPredator(12, vec_p.subtract(vec_p_prime));
			Vec3 field_sum = Vec3.ZERO;
			field_sum = field_sum.add(field_obstacle);
			field_sum = field_sum.add(field_wall);
			field_sum = field_sum.add(field_predator);
    		
    		Vec3 vec_po = vec_p.add(field_obstacle);
    		Vec3 vec_pw = vec_p.add(field_wall);
    		Vec3 vec_pp = vec_p.add(field_predator);
    		Vec3 vec_field = vec_p.add(field_sum);
    		
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_po.subtract(vec_cam), 'x');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_pw.subtract(vec_cam), 'y');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_pp.subtract(vec_cam), 'z');
    		CstRenderComponent.renderLine(ps, vc, vec_p.subtract(vec_cam), vec_field.subtract(vec_cam), 'b');
    	}
    	if (CstAgent.show_path) {
    		Vec3 node_curr = Vec3.ZERO;
    		Vec3 node_next = Vec3.ZERO;
    		CstAgent.getPath();
    		ArrayList<Vec3> path_p = CstAgent.path_prey;
    		ArrayList<Vec3> path_p_prime = CstAgent.path_predator;
    		
			if (path_p.size() >= 2) {
				for (int i=0; i<path_p.size()-1; i++) {
					node_curr = (path_p.get(i)).add(new Vec3(0.5, 0, 0.5));
	    			node_next = (path_p.get(i+1)).add(new Vec3(0.5, 0, 0.5));
	    			CstRenderComponent.renderLine(ps, vc, node_curr.subtract(vec_cam), node_next.subtract(vec_cam), 'g');
	    		}
			}
			if (path_p_prime.size() >= 2) {
				for (int j=0; j<path_p_prime.size()-1; j++) {
					node_curr = (path_p_prime.get(j)).add(new Vec3(0.5, 0, 0.5));
	    			node_next = (path_p_prime.get(j+1)).add(new Vec3(0.5, 0, 0.5));
	    			CstRenderComponent.renderLine(ps, vc, node_curr.subtract(vec_cam), node_next.subtract(vec_cam), 'g');
	    		}
			}
    	}
    	
    }
    
}