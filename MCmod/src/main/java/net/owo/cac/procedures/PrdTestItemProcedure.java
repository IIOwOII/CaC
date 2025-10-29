package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class PrdTestItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String option_tester_text = "";
		option_tester_text = net.owo.cac.CstItem.getItemOptionText(0);
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal((option_tester_text + " is executed!")), true);
		}
		if ((option_tester_text).equals("Increase Difficulty")) {
			if (CacModVariables.MapVariables.get(world).Dat_difficulty_absolute <= 1.5) {
				CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = CacModVariables.MapVariables.get(world).Dat_difficulty_absolute + 0.05;
				CacModVariables.MapVariables.get(world).syncData(world);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Speed Ratio : " + new java.text.DecimalFormat("#.##").format(CacModVariables.MapVariables.get(world).Dat_difficulty_absolute))), false);
			} else {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Maximum Speed!"), false);
			}
		} else if ((option_tester_text).equals("Decrease Difficulty")) {
			if (CacModVariables.MapVariables.get(world).Dat_difficulty_absolute >= 0.5) {
				CacModVariables.MapVariables.get(world).Dat_difficulty_absolute = CacModVariables.MapVariables.get(world).Dat_difficulty_absolute - 0.05;
				CacModVariables.MapVariables.get(world).syncData(world);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Speed Ratio : " + new java.text.DecimalFormat("#.##").format(CacModVariables.MapVariables.get(world).Dat_difficulty_absolute))), false);
			} else {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Minimum Speed!"), false);
			}
		} else if ((option_tester_text).equals("AI Switch")) {
			CacModVariables.MapVariables.get(world).Switch_AI = !CacModVariables.MapVariables.get(world).Switch_AI;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (CacModVariables.MapVariables.get(world).Switch_AI) {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("AI On!"), false);
			} else {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("AI Off!"), false);
			}
		}
	}
}
