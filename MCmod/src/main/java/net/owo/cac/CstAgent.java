package net.owo.cac;

import java.util.ArrayList;
import javax.annotation.Nullable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.owo.cac.CacMod;
import net.owo.cac.CstField;
import net.owo.cac.CstRenderHandler;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.procedures.EvQueImmediateProcedure;
import net.owo.cac.procedures.EvPulseRecordProcedure;
import net.owo.cac.procedures.AiEndProcedure;

import net.owo.cac.entity.EntCatEntity;
import net.owo.cac.entity.EntMouseEntity;
import net.owo.cac.entity.EntPlayerCatEntity;
import net.owo.cac.entity.EntPlayerMouseEntity;
import net.owo.cac.entity.EntPseudoCatEntity;
import net.owo.cac.entity.EntPseudoMouseEntity;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstAgent {
	public static ArrayList<Vec3> path_opponent = new ArrayList<>();
	public static ArrayList<Vec3> path_player = new ArrayList<>();

	// tag (-1: null, 0: cat, 1: mouse
	public static int tag_opponent = -1;
	public static int tag_player = -1;
	
	@Nullable public static Entity ent_opponent = null;
	@Nullable public static Entity ent_player = null;
	public static Vec3 pos_opponent = Vec3.ZERO;
	public static Vec3 pos_player = Vec3.ZERO;
	
	public static int agent_duration_max = 600;
	public static int agent_duration = 0;
	public static double agent_distance = 0;

	public static int TIMELIMIT = 600;
	public static int TimP_sample = 0;
	public static boolean show_path = false;
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		LevelAccessor world = event.player.level();
		@Nullable Entity player = event.player;
		if ((event.phase == TickEvent.Phase.END) && (!world.isClientSide())) {
			if (ent_opponent != null && ent_player != null) {
				pos_opponent = ent_opponent.position();
				pos_player = ent_player.position();
				CacModVariables.Pos_opponent = pos_opponent;
				CacModVariables.Pos_player = pos_player;
				
				if (CacModVariables.Switch_trace) {
					if (CacModVariables.Exp_phase == 1) {
						CacModVariables.Dat_pos_time_prep.add((int)CacModVariables.TimR_time);
						CacModVariables.Dat_pos_player_x_prep.add((pos_player.x()));
						CacModVariables.Dat_pos_player_z_prep.add((pos_player.z()));
						CacModVariables.Dat_pos_player_r_prep.add((ent_player.getYRot()));
						CacModVariables.Dat_pos_opponent_x_prep.add((pos_opponent.x()));
						CacModVariables.Dat_pos_opponent_z_prep.add((pos_opponent.z()));
						CacModVariables.Dat_pos_opponent_r_prep.add((ent_opponent.getYRot()));
					}
					if (CacModVariables.Exp_phase == 2) {
						CacModVariables.Dat_pos_time.add((int)CacModVariables.TimR_time);
						CacModVariables.Dat_pos_player_x.add((pos_player.x()));
						CacModVariables.Dat_pos_player_z.add((pos_player.z()));
						CacModVariables.Dat_pos_player_r.add((ent_player.getYRot()));
						CacModVariables.Dat_pos_opponent_x.add((pos_opponent.x()));
						CacModVariables.Dat_pos_opponent_z.add((pos_opponent.z()));
						CacModVariables.Dat_pos_opponent_r.add((ent_opponent.getYRot()));
					}
				}

				// Move
				if (CacModVariables.Switch_AI) {
					TimP_sample = (TimP_sample + 1) % 4; // move time
					agent_duration = agent_duration - 1;
					agent_distance = (pos_opponent.subtract(pos_player)).length();
					if ((agent_duration <= 0) || (agent_distance < 1)) {
						ent_opponent.setDeltaMovement(Vec3.ZERO);
						ent_player.setDeltaMovement(Vec3.ZERO);
						if (agent_distance < 1) {
							CacModVariables.Ev_pulse_content = "touch";
							EvPulseRecordProcedure.execute();
						}
						EvQueImmediateProcedure.execute();
						AiEndProcedure.execute(world, pos_player.x, pos_player.y, pos_player.z, player);
						CacModVariables.Switch_AI = false;
					}
				}
				
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		@Nullable Entity _ent = event.getEntity();
		if (_ent == null) return;
		if (_ent instanceof EntCatEntity || _ent instanceof EntMouseEntity) {
			ent_opponent = _ent;
		} 
		if ((_ent instanceof EntPlayerCatEntity || _ent instanceof EntPlayerMouseEntity) || (_ent instanceof EntPseudoCatEntity || _ent instanceof EntPseudoMouseEntity)) {
			ent_player = _ent;
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		@Nullable Entity _ent = event.getEntity();
		if ((event != null) && (_ent != null)) {
			if (_ent == ent_opponent)
				ent_opponent = null;
			if (_ent == ent_player)
				ent_player = null;
		}
	}


	// AI move
	public static Vec3 destPrey(boolean is_opponent) {
		Vec3 vec_p_prime = Vec3.ZERO;
		Vec3 vec_p = Vec3.ZERO;
		if (is_opponent) {
			vec_p_prime = pos_player;
			vec_p = pos_opponent;
		} else { // false : pseudomouse
			vec_p_prime = pos_opponent;
			vec_p = pos_player;
		}
		Vec3 field_obstacle = CstField.calFieldObstacle(3, vec_p);
		Vec3 field_wall = CstField.calFieldWall(8, vec_p);
		Vec3 field_player = CstField.calFieldPlayer(12, vec_p.subtract(vec_p_prime));
		Vec3 field_sum = Vec3.ZERO;
		field_sum = field_sum.add(field_obstacle);
		field_sum = field_sum.add(field_wall);
		field_sum = field_sum.add(field_player);
		Vec3 vec_destination = vec_p.add(field_sum);
		return vec_destination;
	}
	public static Vec3 destPredator(boolean is_opponent) {
		Vec3 vec_p_prime = Vec3.ZERO;
		Vec3 vec_p = Vec3.ZERO;
		if (is_opponent) {
			vec_p_prime = pos_player;
			vec_p = pos_opponent;
		} else { // false : pseudocat
			vec_p_prime = pos_opponent;
			vec_p = pos_player;
		}
		Vec3 vec_destination = vec_p_prime;
		return vec_destination;
	}

	
	// get nodes Pathfinders
	public static void getPath(Entity entity, boolean is_opponent, boolean is_predator) {
		@Nullable Path path = null;
		ArrayList<Vec3> vec_nodes = new ArrayList<>();
		Vec3 pos_node = Vec3.ZERO;
		if (entity instanceof Mob mob) {
			path = mob.getNavigation().getPath();
		}
		if (path == null) {
			if (is_opponent) {
				path_opponent = new ArrayList<>();
			} else {
				path_player = new ArrayList<>();
			}
			return;
		}
		for (int i=0; i<path.getNodeCount(); i++) {
			pos_node = path.getNode(i).asVec3();
			vec_nodes.add(pos_node);
		}
		if (is_opponent) {
			path_opponent = new ArrayList<>(vec_nodes);
		} else {
			path_player = new ArrayList<>(vec_nodes);
		}
		return;
	}

	// if cat arrive dest. but not catch, force to move
	public static void pushPredator(Entity entity) {
		return;
	}


	// wind up
	public static void setDuration(int dur) {
		agent_duration_max = dur;
		agent_duration = dur;
	}

	// total trial time
	public static int getDuration() {
		int playtime = agent_duration_max - agent_duration;
		return playtime;
	}

	// win or lose
	public static int getResult() {
		int result = -1;
		int playtime = getDuration();
		if (((playtime < TIMELIMIT) && (CacModVariables.Dat_trial_type == 0)) || ((playtime >= TIMELIMIT) && (CacModVariables.Dat_trial_type == 1))) {
			result = 1; // win
		} else if (((playtime >= TIMELIMIT) && (CacModVariables.Dat_trial_type == 0)) || ((playtime < TIMELIMIT) && (CacModVariables.Dat_trial_type == 1))) {
			result = 0; // lose
		}
		return result;
	}
	
}
