package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimCountdownProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		if (CacModVariables.TimC_switch && !world.isClientSide()) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("title @a title " + new java.text.DecimalFormat("##").format(Math.ceil(CacModVariables.TimC_time / 20))));
			CacModVariables.TimC_time = CacModVariables.TimC_time - 1;
			if (CacModVariables.TimC_time == 0) {
				if ((CacModVariables.TimC_que).equals("tutorial_beginner")) {
					TutoBeginnerStartProcedure.execute();
				} else if ((CacModVariables.TimC_que).equals("tutorial_checkpoint")) {
					TutoCheckpointStartProcedure.execute(world, x, y, z);
				} else if ((CacModVariables.TimC_que).equals("tutorial_racing")) {
					TutoRacingStartProcedure.execute(world, x, y, z);
				}
				CacModVariables.TimC_que = "";
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"title @a clear");
				CacModVariables.TimC_switch = false;
			}
		}
	}
}
