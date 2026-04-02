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
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.owo.cac.CacMod;
import net.owo.cac.CstField;
import net.owo.cac.CstRenderHandler;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.procedures.EvQueImmediateProcedure;
import net.owo.cac.procedures.EffApplyStopMoveProcedure;
import net.owo.cac.procedures.EvPulseRecordProcedure;
import net.owo.cac.procedures.MeowMoveOffProcedure;

import net.owo.cac.entity.EntCatEntity;
import net.owo.cac.entity.EntMouseEntity;
import net.owo.cac.entity.EntPlayerCatEntity;
import net.owo.cac.entity.EntPlayerMouseEntity;
import net.owo.cac.entity.EntPseudoCatEntity;
import net.owo.cac.entity.EntPseudoMouseEntity;



@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstAgent {
	public static int TIMELIMIT = 600;
	public static double CONST_SPEED = 0.48989794855;
	public static int CONST_PERIOD = 4; //

	@Nullable public static Mob ent_predator = null;
	@Nullable public static Mob ent_prey = null;
	public static int label_predator = -1;
	public static int label_prey = -1;
	public static ArrayList<Vec3> path_predator = new ArrayList<>();
	public static ArrayList<Vec3> path_prey = new ArrayList<>();
	
	public static int agent_duration_max = 600;
	public static int agent_duration = 0;
	public static double agent_distance = 0;
	
	public static boolean show_path = false;
	public static int timAi = 0;
	
	public static boolean getSwitchAi() {return CacModVariables.Switch_AI;}
	public static double getRho() {return CacModVariables.Dat_difficulty;}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		@Nullable Entity player = event.player;
		LevelAccessor world = event.player.level();
		if (world.isClientSide() || ent_predator == null || ent_prey == null) return;
		if (event.phase == TickEvent.Phase.END) {
			if (CacModVariables.Switch_trace) {
				if (CacModVariables.Exp_phase == 1) {
					CacModVariables.Dat_pos_time_prep.add((int)CacModVariables.TimR_time);
					CacModVariables.Dat_predator_x_prep.add(ent_predator.getX());
					CacModVariables.Dat_predator_z_prep.add(ent_predator.getZ());
					CacModVariables.Dat_predator_r_prep.add((ent_predator.getYRot()));
					CacModVariables.Dat_prey_x_prep.add(ent_prey.getX());
					CacModVariables.Dat_prey_z_prep.add(ent_prey.getZ());
					CacModVariables.Dat_prey_r_prep.add(ent_prey.getYRot());
				} else if (CacModVariables.Exp_phase == 2) {
					CacModVariables.Dat_pos_time.add((int)CacModVariables.TimR_time);
					CacModVariables.Dat_predator_x.add(ent_predator.getX());
					CacModVariables.Dat_predator_z.add(ent_predator.getZ());
					CacModVariables.Dat_predator_r.add(ent_predator.getYRot());
					CacModVariables.Dat_prey_x.add(ent_prey.getX());
					CacModVariables.Dat_prey_z.add(ent_prey.getZ());
					CacModVariables.Dat_prey_r.add(ent_prey.getYRot());
				}
			}
			
			// Move
			if (getSwitchAi()) {
				timAi++;
				tickPredator(world);
				tickPrey(world);
				agent_duration = agent_duration - 1;
				agent_distance = ((ent_predator.position()).subtract(ent_prey.position())).length();
				if ((agent_duration <= 0) || (agent_distance < 1)) { // end
					EffApplyStopMoveProcedure.execute(player);
					if (agent_distance < 1) {
						CacModVariables.Ev_pulse_content = "touch";
						EvPulseRecordProcedure.execute();
					}
					EvQueImmediateProcedure.execute();
					MeowMoveOffProcedure.execute();
					CacModVariables.Dat_time_gameplay = getDuration();
					CacModVariables.Dat_trial_winlose = getResult();
					// Sound
					if (world instanceof Level _level) {
						if (CacModVariables.Dat_trial_winlose == 1) {
							_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_a7")), SoundSource.NEUTRAL, 1, 1);
						} else if (CacModVariables.Dat_trial_winlose == 0) {
							_level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("cac:snd_orb_c7")), SoundSource.NEUTRAL, 1, 1);
						}
					}
					// Switch off
					CacModVariables.Switch_AI = false;
					CacModVariables.Switch_trace = false;
					timAi = 0;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide()) return;
		@Nullable Entity _ent = event.getEntity();
		if (_ent == null) return;
		if (_ent instanceof EntCatEntity) {
			ent_predator = (_ent instanceof Mob pent ? pent : null);
			label_predator = 0;
		}
		if (_ent instanceof EntPlayerCatEntity) {
			ent_predator = (_ent instanceof Mob pent ? pent : null);
			label_predator = 1;
		}
		if (_ent instanceof EntPseudoCatEntity) {
			ent_predator = (_ent instanceof Mob pent ? pent : null);
			label_predator = 2;
		}
		if (_ent instanceof EntMouseEntity) {
			ent_prey = (_ent instanceof Mob pent ? pent : null);
			label_prey = 0;
		}
		if (_ent instanceof EntPlayerMouseEntity) {
			ent_prey = (_ent instanceof Mob pent ? pent : null);
			label_prey = 1;
		}
		if (_ent instanceof EntPseudoMouseEntity) {
			ent_prey = (_ent instanceof Mob pent ? pent : null);
			label_prey = 2;
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		@Nullable Entity _ent = event.getEntity();
		if (event == null || _ent == null) return;
		if (_ent == ent_predator) {
			ent_predator = null;
			label_predator = -1;
		}
		if (_ent == ent_prey) {
			ent_prey = null;
			label_prey = -1;
		}
	}


	// AI move
	public static void tickPredator(LevelAccessor world) {
		if (label_predator == 0) {
			movePredator(world, CONST_SPEED * Math.pow(getRho(), 0.5));
		} else if (label_predator == 2) {
			movePredator(world, CONST_SPEED);
		}
	}
	public static void movePredator(LevelAccessor world, double speed) {
		if (world.isClientSide() || ent_prey == null || ent_predator == null) return;
		Vec3 dest = destPredator();
		ent_predator.getNavigation().moveTo(dest.x(), dest.y(), dest.z(), speed);
	}
	public static Vec3 destPredator() {
		Vec3 vec_predator = Vec3.ZERO;
		Vec3 vec_prey = Vec3.ZERO;
		vec_predator = ent_predator.position();
		vec_prey = ent_prey.position();
		Vec3 vec_pp = vec_prey.subtract(vec_predator);
		Vec3 vec_destination = vec_prey.add(vec_pp.normalize());
		return vec_destination;
	}

	public static void tickPrey(LevelAccessor world) {
		if (label_prey == 0) {
			movePrey(world, CONST_SPEED * Math.pow(getRho(), 0.5));
		} else if (label_prey == 2) {
			movePrey(world, CONST_SPEED);
		}
	}
	public static void movePrey(LevelAccessor world, double speed) {
		if (world.isClientSide() || ent_prey == null || ent_predator == null) return;
		Vec3 dest = destPrey();
		ent_prey.getNavigation().moveTo(dest.x(), dest.y(), dest.z(), speed);
	}
	public static Vec3 destPrey() {
		Vec3 vec_predator = Vec3.ZERO;
		Vec3 vec_prey = Vec3.ZERO;
		vec_predator = ent_predator.position();
		vec_prey = ent_prey.position();
		
		Vec3 field_obstacle = CstField.calFieldObstacle(3, vec_prey);
		Vec3 field_wall = CstField.calFieldWall(7, vec_prey);
		Vec3 field_predator = CstField.calFieldPredator(10, vec_prey.subtract(vec_predator));
		Vec3 field_sum = Vec3.ZERO;
		field_sum = field_sum.add(field_obstacle);
		field_sum = field_sum.add(field_wall);
		field_sum = field_sum.add(field_predator);
		Vec3 vec_destination = vec_prey.add(field_sum);
		return vec_destination;
	}

	
	// get nodes Pathfinders
	public static void getPath() {
		@Nullable Path rawpath_predator = null;
		@Nullable Path rawpath_prey = null;
		ArrayList<Vec3> node_predator = new ArrayList<>();
		ArrayList<Vec3> node_prey = new ArrayList<>();
		
		if (ent_predator != null) rawpath_predator = ent_predator.getNavigation().getPath();
		if (ent_prey != null) rawpath_prey = ent_prey.getNavigation().getPath();
		
		if (rawpath_predator != null) {
			for (int i=0; i<rawpath_predator.getNodeCount(); i++) {
				node_predator.add(rawpath_predator.getNode(i).asVec3());
			}
			path_predator = new ArrayList<>(node_predator);
		} else {
			path_predator = new ArrayList<>();
		}
		if (rawpath_prey != null) {
			for (int i=0; i<rawpath_prey.getNodeCount(); i++) {
				node_prey.add(rawpath_prey.getNode(i).asVec3());
			}
			path_prey = new ArrayList<>(node_prey);
		} else {
			path_prey = new ArrayList<>();
		}
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