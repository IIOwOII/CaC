package net.owo.cac;

import net.owo.cac.CacMod;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.entity.EntCatEntity;
import net.owo.cac.entity.EntMouseEntity;
import net.owo.cac.entity.EntPlayerCatEntity;
import net.owo.cac.entity.EntPlayerMouseEntity;

@Mod.EventBusSubscriber
public class CstManagePosition {
	@Nullable public static Entity ent_opponent = null;
	@Nullable public static Entity ent_player = null;
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			LevelAccessor world = event.player.level();
			if (ent_opponent != null) {
				CacModVariables.MapVariables.get(world).Pos_opponent = ent_opponent.position();
				CacModVariables.MapVariables.get(world).syncData(world);
			}
			if (ent_player != null) {
				CacModVariables.MapVariables.get(world).Pos_player = ent_player.position();
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
		if (_ent instanceof EntPlayerCatEntity || _ent instanceof EntPlayerMouseEntity) {
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
