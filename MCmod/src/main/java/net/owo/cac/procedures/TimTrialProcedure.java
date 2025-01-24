package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TimTrialProcedure {
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
		if (CacModVariables.MapVariables.get(world).Tim_trial_switch) {
			CacModVariables.MapVariables.get(world).Tim_trial_time = CacModVariables.MapVariables.get(world).Tim_trial_time + 0.05;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Tim_trial_time >= 30) {
				CacModVariables.MapVariables.get(world).Tim_trial_switch = false;
				CacModVariables.MapVariables.get(world).syncData(world);
				CacModVariables.MapVariables.get(world).Switch_AI = false;
				CacModVariables.MapVariables.get(world).syncData(world);
				if (!world.isClientSide()) {
					if (world instanceof Level _level) {
						if (_level.isClientSide()) {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
						}
					}
				}
				TaskPhaseEndProcedure.execute(world);
			}
		}
	}
}
