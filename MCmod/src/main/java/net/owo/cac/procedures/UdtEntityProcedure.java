package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class UdtEntityProcedure {
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getStringUUID()).equals(CacModVariables.MapVariables.get(world).UUID_player)) {
			CacModVariables.MapVariables.get(world).Pos_player_x = entity.getX();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Pos_player_z = entity.getZ();
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if ((entity.getStringUUID()).equals(CacModVariables.MapVariables.get(world).UUID_opponent)) {
			CacModVariables.MapVariables.get(world).Pos_opponent_x = entity.getX();
			CacModVariables.MapVariables.get(world).syncData(world);
			CacModVariables.MapVariables.get(world).Pos_opponent_z = entity.getZ();
			CacModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
