package net.owo.cac;

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

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstField {
	public static final double MAX_FIELD = 30.0;
	public static final double MAX_FIELD_PLAYER = 10.0;
	
	public static boolean show_field = false;
	public static boolean show_obstacle = false;
	public static ArrayList<ArrayList<Vec3>> list_obstacle = new ArrayList<>();
	public static ArrayList<ArrayList<Vec3>> list_wall = new ArrayList<>();
	public static ArrayList<Vec3> points_obstacle = new ArrayList<>();
	public static ArrayList<Vec3> points_wall = new ArrayList<>();
	public static ArrayList<Vec3> points_parasol = new ArrayList<>();
	public static ArrayList<Vec3> points_grass = new ArrayList<>();
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

	// record
	public static JsonArray vec2arr(Vec3 vec) {
		JsonArray arr = new JsonArray();
		arr.add(vec.x());
		arr.add(vec.z());
		return arr;
	}
	public static void recObstacle() {
		JsonObject obj_file = new JsonObject();
		JsonArray arr_border = new JsonArray();
		JsonArray arr_obstacle = new JsonArray();
		JsonArray arr_wall = new JsonArray();
		JsonArray arr_obstacle_point = new JsonArray();
		JsonArray arr_wall_point = new JsonArray();
		JsonArray arr_parasol_point = new JsonArray();
		JsonArray arr_grass_point = new JsonArray();
		JsonArray arr_temp = new JsonArray();

		// create file
		File Info_obstacle = new File(CacModVariables.Dir_components, File.separator + "info_obstacle.json");
		try {
			Info_obstacle.getParentFile().mkdirs();
			Info_obstacle.createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		// add components
		arr_border.add(vec2arr(pos_border_start));
		arr_border.add(vec2arr(pos_border_end));
		for (int i=0; i<list_obstacle.size(); i++) {
			arr_temp = new JsonArray();
			arr_temp.add(vec2arr(list_obstacle.get(i).get(0)));
			arr_temp.add(vec2arr(list_obstacle.get(i).get(1)));
			arr_obstacle.add(arr_temp.deepCopy());
		}
		for (int j=0; j<list_wall.size(); j++) {
			arr_temp = new JsonArray();
			arr_temp.add(vec2arr(list_wall.get(j).get(0)));
			arr_temp.add(vec2arr(list_wall.get(j).get(1)));
			arr_wall.add(arr_temp.deepCopy());
		}
		for (int i=0; i<points_obstacle.size(); i++) {
			arr_obstacle_point.add(vec2arr(points_obstacle.get(i)));
		}
		for (int j=0; j<points_wall.size(); j++) {
			arr_wall_point.add(vec2arr(points_wall.get(j)));
		}
		if (points_parasol.size() != 0) {
			for (int k=0; k<points_parasol.size(); k++) {
				arr_parasol_point.add(vec2arr(points_parasol.get(k)));
			}
		}
		if (points_grass.size() != 0) {
			for (int l=0; l<points_grass.size(); l++) {
				arr_grass_point.add(vec2arr(points_grass.get(l)));
			}
		}

		// write
		obj_file.add("border", arr_border);
		obj_file.add("obstacle", arr_obstacle);
		obj_file.add("wall", arr_wall);
		obj_file.add("obstacle_point", arr_obstacle_point);
		obj_file.add("wall_point", arr_wall_point);
		obj_file.add("parasol_point", arr_parasol_point);
		obj_file.add("grass_point", arr_grass_point);
		com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fileWriter = new FileWriter(Info_obstacle);
			fileWriter.write(mainGSONBuilderVariable.toJson(obj_file));
			fileWriter.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	// calculate field
	public static Vec3 calFieldObstacle(double sca_k, Vec3 pos_p) {
		if (list_obstacle.size() == 0) return Vec3.ZERO;
		// double alpha = Math.pow(MAX_FIELD/(2*sca_k), 2);
		Vec3 vec_p = Vec3.ZERO;
		vec_p = new Vec3(pos_p.x()-0.5, 64.0, pos_p.z()-0.5);
		Vec3 vec_a = Vec3.ZERO;
		Vec3 vec_b = Vec3.ZERO;
		Vec3 vec_m = Vec3.ZERO;
		Vec3 basis_m = Vec3.ZERO;
		Vec3 vec_field = Vec3.ZERO;
		double cos_w = 0;
		double cond_w = 0;
		double R_w = 0;
		double R_a = 0;
		double R_b = 0;
		double R_ab = 0;
		for (int i=0; i<list_obstacle.size(); i++) {
			vec_a = (list_obstacle.get(i).get(0)).subtract(vec_p);
			vec_b = (list_obstacle.get(i).get(1)).subtract(vec_p);
			R_a = vec_a.length();
			R_b = vec_b.length();
			R_ab = R_a*R_b;
			vec_m = (vec_a.scale(-R_b/(R_a+R_b))).add(vec_b.scale(-R_a/(R_a+R_b)));
			basis_m = vec_m.normalize();
			/*
			cond_w = (0.5*(Math.pow(R_a,2)+Math.pow(R_b,2)) - alpha*Math.pow(R_ab,2))/(1+alpha*R_ab);
			if (vec_a.dot(vec_b) < cond_w) { // over than MAX
				vec_field = vec_field.add(basis_m.scale(MAX_FIELD));
				continue;
			}
			*/
			cos_w = Math.pow(0.5 + (vec_a.dot(vec_b))/(2*R_ab), 0.5);
			R_w = (vec_b.subtract(vec_a)).length();
			vec_field = vec_field.add(basis_m.scale((sca_k*R_w)/(R_ab*cos_w)));
		}
		if (vec_field.length() > MAX_FIELD) {
			vec_field = (vec_field.normalize()).scale(MAX_FIELD);
		}
		return vec_field;
	}
	public static Vec3 calFieldWall(double sca_k, Vec3 pos_p) {
		if (list_wall.size() == 0) return Vec3.ZERO;
		// double alpha = Math.pow(MAX_FIELD/(2*sca_k), 2);
		Vec3 vec_p = Vec3.ZERO;
		vec_p = new Vec3(pos_p.x()-0.5, 64.0, pos_p.z()-0.5);
		Vec3 vec_a = Vec3.ZERO;
		Vec3 vec_b = Vec3.ZERO;
		Vec3 vec_m = Vec3.ZERO;
		Vec3 basis_m = Vec3.ZERO;
		Vec3 vec_field = Vec3.ZERO;
		double cos_w = 0;
		double cond_w = 0;
		double R_w = 0;
		double R_a = 0;
		double R_b = 0;
		double R_ab = 0;
		for (int i=0; i<list_wall.size(); i++) {
			vec_a = (list_wall.get(i).get(0)).subtract(vec_p);
			vec_b = (list_wall.get(i).get(1)).subtract(vec_p);
			R_a = vec_a.length();
			R_b = vec_b.length();
			R_ab = R_a*R_b;
			vec_m = (vec_a.scale(-R_b/(R_a+R_b))).add(vec_b.scale(-R_a/(R_a+R_b)));
			basis_m = vec_m.normalize();
			/*
			cond_w = (0.5*(Math.pow(R_a,2)+Math.pow(R_b,2)) - alpha*Math.pow(R_ab,2))/(1+alpha*R_ab);
			if (vec_a.dot(vec_b) < cond_w) { // over than MAX
				vec_field = vec_field.add(basis_m.scale(MAX_FIELD));
				continue;
			}
			*/
			cos_w = Math.pow(0.5 + (vec_a.dot(vec_b))/(2*R_ab), 0.5);
			R_w = (vec_b.subtract(vec_a)).length();
			vec_field = vec_field.add(basis_m.scale((sca_k*R_w)/(R_ab*cos_w)));
		}
		if (vec_field.length() > MAX_FIELD) {
			vec_field = (vec_field.normalize()).scale(MAX_FIELD);
		}
		return vec_field;
	}
	public static Vec3 calFieldPredator(double sca_k, Vec3 vec_pp) {
		double R_sqr = vec_pp.lengthSqr();
		Vec3 vec_field = Vec3.ZERO;
		vec_field = vec_pp.scale(sca_k/R_sqr);
		if (vec_field.length() > MAX_FIELD_PLAYER) {
			vec_field = (vec_field.normalize()).scale(MAX_FIELD_PLAYER);
		}
		return vec_field;
	}
	

	// Scan Obstacle
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
		double py = oy+5; // parasol y
		int bx0 = (int) Math.round(pos_border_start.x() - 0.5); // bound pos x (start)
		int bx1 = (int) Math.round(pos_border_end.x() - 0.5); // bound pos x (end)
		int bz0 = (int) Math.round(pos_border_start.z() - 0.5); // bound pos z (start)
		int bz1 = (int) Math.round(pos_border_end.z() - 0.5); // bound pos z (end)

		// reset
		list_obstacle.clear();
		list_wall.clear();
		points_obstacle.clear();
		points_wall.clear();
		points_parasol.clear();
		points_grass.clear();

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
					points_obstacle.add(new Vec3(cx, oy, cz));
					if (!(block_prev.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_OBSTACLE.get())) ispoint_end = true;
				}
				if (block_curr.getBlock() == CacModBlocks.BLK_WALL.get()) { // wall
					points_wall.add(new Vec3(cx, oy, cz));
					if (!(block_prev.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_start = true;
					if (!(block_next.getBlock() == CacModBlocks.BLK_WALL.get())) iswall_end = true;
				}
				if ((world.getBlockState(BlockPos.containing(cx, py, cz))).getBlock() == CacModBlocks.BLK_PARASOL.get()) { // parasol
					points_parasol.add(new Vec3(cx, py, cz));
				}
				if ((block_curr.getBlock() == CacModBlocks.BLK_AZALEA_GRASS.get()) || (block_curr.getBlock() == CacModBlocks.BLK_FLOWERING_AZALEA_GRASS.get())) { // grass
					points_grass.add(new Vec3(cx, oy, cz));
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
