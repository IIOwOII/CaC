package net.owo.cac;

import javax.annotation.Nullable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;

import net.owo.cac.CacMod;
import net.owo.cac.network.CacModVariables;
import net.owo.cac.init.CacModMobEffects;

import net.owo.cac.entity.EntCatEntity;
import net.owo.cac.entity.EntMouseEntity;
import net.owo.cac.entity.EntPlayerCatEntity;
import net.owo.cac.entity.EntPlayerMouseEntity;
import net.owo.cac.entity.EntPseudoCatEntity;
import net.owo.cac.entity.EntPseudoMouseEntity;
import net.owo.cac.procedures.EvPulseRecordProcedure;
import net.owo.cac.procedures.EvQueImmediateProcedure;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstManagePosition {
	@Nullable public static Entity ent_opponent = null;
	@Nullable public static Entity ent_player = null;
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			LevelAccessor world = event.player.level();
			
			if ((ent_opponent != null && ent_player != null) && (!world.isClientSide())) {
				Vec3 pos_opponent = ent_opponent.position();
				Vec3 pos_player = ent_player.position();

				if (CacModVariables.MapVariables.get(world).Switch_trace) {
					if (CacModVariables.MapVariables.get(world).Exp_phase == 1) {
						CacModVariables.Dat_pos_time_prep.add((int)CacModVariables.MapVariables.get(world).TimR_time);
						CacModVariables.Dat_pos_player_x_prep.add((pos_player.x()));
						CacModVariables.Dat_pos_player_z_prep.add((pos_player.z()));
						CacModVariables.Dat_pos_player_r_prep.add((ent_player.getYRot()));
						CacModVariables.Dat_pos_opponent_x_prep.add((pos_opponent.x()));
						CacModVariables.Dat_pos_opponent_z_prep.add((pos_opponent.z()));
						CacModVariables.Dat_pos_opponent_r_prep.add((ent_opponent.getYRot()));
					}
					if (CacModVariables.MapVariables.get(world).Exp_phase == 2) {
						CacModVariables.Dat_pos_time.add((int)CacModVariables.MapVariables.get(world).TimR_time);
						CacModVariables.Dat_pos_player_x.add((pos_player.x()));
						CacModVariables.Dat_pos_player_z.add((pos_player.z()));
						CacModVariables.Dat_pos_player_r.add((ent_player.getYRot()));
						CacModVariables.Dat_pos_opponent_x.add((pos_opponent.x()));
						CacModVariables.Dat_pos_opponent_z.add((pos_opponent.z()));
						CacModVariables.Dat_pos_opponent_r.add((ent_opponent.getYRot()));
					}
				}
				
				if (CacModVariables.MapVariables.get(world).Switch_AI && (pos_opponent.subtract(pos_player)).length() < 1){
					CacModVariables.MapVariables.get(world).Switch_AI = false;
					CacModVariables.MapVariables.get(world).syncData(world);
					CacModVariables.MapVariables.get(world).Ev_pulse_content = "touch";
					CacModVariables.MapVariables.get(world).syncData(world);
					if (CacModVariables.MapVariables.get(world).Switch_trace) {
						EvPulseRecordProcedure.execute(world);
						EvQueImmediateProcedure.execute(world);
					}
				}
				
				CacModVariables.MapVariables.get(world).Pos_opponent = pos_opponent;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Pos_player = pos_player;
				CacModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		@Nullable Entity _ent;
		_ent = event.getEntity();
		if (_ent == null)
			return;
			
		if (_ent instanceof EntCatEntity || _ent instanceof EntMouseEntity) {
			ent_opponent = _ent;
		} 
		if ((_ent instanceof EntPlayerCatEntity || _ent instanceof EntPlayerMouseEntity) || (_ent instanceof EntPseudoCatEntity || _ent instanceof EntPseudoMouseEntity)) {
			ent_player = _ent;
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		@Nullable Entity _ent;
		_ent = event.getEntity();
		if (_ent == null)
			return;
		
		if (event != null) {
			if (_ent == ent_opponent) {
				ent_opponent = null;
			}
			if (_ent == ent_player) {
				ent_player = null;
			}
		}
	}
}
