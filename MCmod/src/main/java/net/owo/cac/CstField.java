package net.owo.cac;

import java.util.ArrayList;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModBlocks;
import net.owo.cac.CacMod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstField {
	public static boolean show_field = false;
	public static ArrayList<ArrayList<Vec3>> list_obstacle = new ArrayList<>();
	public static ArrayList<ArrayList<Vec3>> list_wall = new ArrayList<>();
	public static Vec3 pos_border_start = new Vec3(-15.5, 64.0, -65.5);
	public static Vec3 pos_border_end = new Vec3(16.5, 64.0, -33.5);
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		LevelAccessor world = event.getEntity().level();
		scanObstacle(world);
	}

	public static void debugObstacle() {
		for (int i=0; i<list_obstacle.size(); i++) {
			CacMod.LOGGER.info(list_obstacle.get(i));
		}
		for (int j=0; j<list_wall.size(); j++) {
			CacMod.LOGGER.info(list_wall.get(j));
		}
	}

	public static Vec3 calFieldObstacle(double sca_k, Vec3 pos_p) {
		if (list_obstacle.size() == 0) return Vec3.ZERO;
		Vec3 vec_p = Vec3.ZERO;
		vec_p = new Vec3(pos_p.x()-0.5, 64.0, pos_p.z()-0.5);
		
		Vec3 vec_a = Vec3.ZERO;
		Vec3 vec_b = Vec3.ZERO;
		Vec3 basis_u = Vec3.ZERO;
		Vec3 basis_n = Vec3.ZERO;
		Vec3 vec_h = Vec3.ZERO;
		Vec3 vec_u = Vec3.ZERO;
		Vec3 vec_n = Vec3.ZERO;
		Vec3 vec_field = Vec3.ZERO;
		double l_a = 0;
		double l_b = 0;
		double L = 0;
		double D = 0;
		double R_a = 0;
		double R_b = 0;
		for (int i=0; i<list_obstacle.size(); i++) {
			vec_a = (list_obstacle.get(i).get(0)).subtract(vec_p);
			vec_b = (list_obstacle.get(i).get(1)).subtract(vec_p);
			R_a = vec_a.length();
			R_b = vec_b.length();
			L = (vec_a.subtract(vec_b)).length();
			basis_u = (vec_a.subtract(vec_b)).normalize();
			l_a = vec_a.dot(basis_u);
			l_b = vec_b.dot(basis_u);
			vec_h = (vec_b.scale(l_a/L)).subtract(vec_a.scale(l_b/L));
			D = vec_h.length();
			
			vec_u = basis_u.scale(sca_k*((1/R_a)-(1/R_b)));
			if (D > 0.1) {
				basis_n = vec_h.normalize();
				vec_n = basis_n.scale(sca_k*((l_a/(D*R_a))-(l_b/(D*R_b))));
				vec_field = vec_field.add(vec_u.subtract(vec_n));
			} else {
				vec_field = vec_field.add(vec_u);
			}
		}
		return vec_field;
	}

	public static Vec3 calFieldWall(double sca_k, Vec3 pos_p) {
		if (list_wall.size() == 0) return Vec3.ZERO;
		Vec3 vec_p = Vec3.ZERO;
		vec_p = new Vec3(pos_p.x()-0.5, 64.0, pos_p.z()-0.5);
		
		Vec3 vec_a = Vec3.ZERO;
		Vec3 vec_b = Vec3.ZERO;
		Vec3 basis_u = Vec3.ZERO;
		Vec3 basis_n = Vec3.ZERO;
		Vec3 vec_h = Vec3.ZERO;
		Vec3 vec_u = Vec3.ZERO;
		Vec3 vec_n = Vec3.ZERO;
		Vec3 vec_field = Vec3.ZERO;
		double l_a = 0;
		double l_b = 0;
		double L = 0;
		double D = 0;
		double R_a = 0;
		double R_b = 0;
		for (int i=0; i<list_wall.size(); i++) {
			vec_a = (list_wall.get(i).get(0)).subtract(vec_p);
			vec_b = (list_wall.get(i).get(1)).subtract(vec_p);
			R_a = vec_a.length();
			R_b = vec_b.length();
			L = (vec_a.subtract(vec_b)).length();
			basis_u = (vec_a.subtract(vec_b)).normalize();
			l_a = vec_a.dot(basis_u);
			l_b = vec_b.dot(basis_u);
			vec_h = (vec_b.scale(l_a/L)).subtract(vec_a.scale(l_b/L));
			D = vec_h.length();
			
			vec_u = basis_u.scale(sca_k*((1/R_a)-(1/R_b)));
			if (D > 0.1) {
				basis_n = vec_h.normalize();
				vec_n = basis_n.scale(sca_k*((l_a/(D*R_a))-(l_b/(D*R_b))));
				vec_field = vec_field.add(vec_u.subtract(vec_n));
			} else {
				vec_field = vec_field.add(vec_u);
			}
		}
		return vec_field;
	}

	public static Vec3 calFieldPlayer(double sca_k, Vec3 vec_pp) {
		double R_sqr = vec_pp.lengthSqr();
		Vec3 vec_field = Vec3.ZERO;
		vec_field = vec_pp.scale(sca_k/R_sqr);
		return vec_field;
	}
	
	public static void scanObstacle(LevelAccessor world) {
		// variables
		BlockState block_curr = Blocks.AIR.defaultBlockState();
		BlockState block_next = Blocks.AIR.defaultBlockState();
		BlockState block_prev = Blocks.AIR.defaultBlockState();
		
		boolean ispoint_start = false;
		boolean ispoint_end = false;
		boolean iswall_start = false;
		boolean iswall_end = false;
		
		double oy = pos_border_start.y();
		int bx0 = (int) Math.round(pos_border_start.x() - 0.5); // bound pos x (start)
		int bx1 = (int) Math.round(pos_border_end.x() - 0.5); // bound pos x (end)
		int bz0 = (int) Math.round(pos_border_start.z() - 0.5); // bound pos z (start)
		int bz1 = (int) Math.round(pos_border_end.z() - 0.5); // bound pos z (end)

		// scan
		Vec3 vec_vertice_temp = Vec3.ZERO;
		Vec3 vec_wall_temp = Vec3.ZERO;
		ArrayList<Vec3> list_obstacle_temp = new ArrayList<>();
		ArrayList<Vec3> list_wall_temp = new ArrayList<>();
		// scan column
		for (int cx=bx0; cx<bx1+1; cx++) {
			block_prev = (world.getBlockState(BlockPos.containing(cx, oy, bz0-1)));
			block_curr = (world.getBlockState(BlockPos.containing(cx, oy, bz0)));
			for (int cz=bz0; cz<bz1+1; cz++) {
				ispoint_start = false;
				ispoint_end = false;
				iswall_start = false;
				iswall_end = false;
				block_next = (world.getBlockState(BlockPos.containing(cx, oy, cz+1)));
				if (block_curr.getBlock() == CacModBlocks.BLK_OBSTACLE.get()) { // obstacle
					if (!(block_prev.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_end = true;
				}
				if (block_curr.getBlock() == CacModBlocks.BLK_WALL.get()) { // wall
					if (!(block_prev.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_end = true;
				}
				if (ispoint_start || ispoint_end) { // if it is vertice (obs)
					vec_vertice_temp = new Vec3(cx, oy, cz);
					if (ispoint_start && !ispoint_end) { // start
						list_obstacle_temp.add(0, vec_vertice_temp);
					} else if (!ispoint_start && ispoint_end) { // end
						list_obstacle_temp.add(1, vec_vertice_temp);
						list_obstacle.add(new ArrayList<>(list_obstacle_temp));
						list_obstacle_temp = new ArrayList<>();
					}
				}
				if (iswall_start || iswall_end) { // if it is vertice (wall)
					vec_wall_temp = new Vec3(cx, oy, cz);
					if (iswall_start && !iswall_end) { // start
						list_wall_temp.add(0, vec_wall_temp);
					} else if (!iswall_start && iswall_end) { // end
						list_wall_temp.add(1, vec_wall_temp);
						list_wall.add(new ArrayList<>(list_wall_temp));
						list_wall_temp = new ArrayList<>();
					}
				}
				block_prev = block_curr;
				block_curr = block_next;
			}
		}
		// scan row
		for (int rz=bz0; rz<bz1+1; rz++) {
			block_prev = (world.getBlockState(BlockPos.containing(bx0-1, oy, rz)));
			block_curr = (world.getBlockState(BlockPos.containing(bx0, oy, rz)));
			for (int rx=bx0; rx<bx1+1; rx++) {
				ispoint_start = false;
				ispoint_end = false;
				iswall_start = false;
				iswall_end = false;
				block_next = (world.getBlockState(BlockPos.containing(rx+1, oy, rz)));
				if (block_curr.getBlock() == CacModBlocks.BLK_OBSTACLE.get()) {
					if (!(block_prev.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_end = true;
				}
				if (block_curr.getBlock() == CacModBlocks.BLK_WALL.get()) { // wall
					if (!(block_prev.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_end = true;
				}
				if (ispoint_start || ispoint_end) { // if it is vertice
					vec_vertice_temp = new Vec3(rx, oy, rz);
					if (ispoint_start && !ispoint_end) { // start
						list_obstacle_temp.add(0, vec_vertice_temp);
					} else if (!ispoint_start && ispoint_end) { // end
						list_obstacle_temp.add(1, vec_vertice_temp);
						list_obstacle.add(new ArrayList<>(list_obstacle_temp));
						list_obstacle_temp = new ArrayList<>();
					}
				}
				if (iswall_start || iswall_end) { // if it is vertice (wall)
					vec_wall_temp = new Vec3(rx, oy, rz);
					if (iswall_start && !iswall_end) { // start
						list_wall_temp.add(0, vec_wall_temp);
					} else if (!iswall_start && iswall_end) { // end
						list_wall_temp.add(1, vec_wall_temp);
						list_wall.add(new ArrayList<>(list_wall_temp));
						list_wall_temp = new ArrayList<>();
					}
				}
				block_prev = block_curr;
				block_curr = block_next;
			}
		}
	}
	
}
