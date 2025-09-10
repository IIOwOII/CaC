package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimRelativeProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (CacModVariables.MapVariables.get(world).Switch_timer && !world.isClientSide()) {
			CacModVariables.MapVariables.get(world).TimR_time = CacModVariables.MapVariables.get(world).TimR_time + 1;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Switch_countdown) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
										_ent.level().getServer(), _ent),
								("title @p subtitle " + new java.text.DecimalFormat("##").format((int) ((CacModVariables.MapVariables.get(world).TimR_que_time - CacModVariables.MapVariables.get(world).TimR_time) / 20 + 1))));
					}
				}
				if (CacModVariables.MapVariables.get(world).TimR_que_time - CacModVariables.MapVariables.get(world).TimR_time == 1) {
					CacModVariables.MapVariables.get(world).Switch_countdown = false;
					CacModVariables.MapVariables.get(world).syncData(world);
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "title @p clear");
						}
					}
				}
			}
			if (CacModVariables.MapVariables.get(world).Switch_que && CacModVariables.MapVariables.get(world).TimR_time == CacModVariables.MapVariables.get(world).TimR_que_time) {
				EvQueCallProcedure.execute(world, x, y, z, entity);
			}
		}
	}
}
