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
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal((CacModVariables.MapVariables.get(world).Option_tester_str + " is executed!")), true);
		}
		if ((CacModVariables.MapVariables.get(world).Option_tester_str).equals("Increase Difficulty")) {
			CacModVariables.MapVariables.get(world).Pmt_difficulty = CacModVariables.MapVariables.get(world).Pmt_difficulty + 0.01;
			CacModVariables.MapVariables.get(world).syncData(world);
			if (!world.isClientSide() && world.getServer() != null)
				world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Difficulty : " + new java.text.DecimalFormat("#.##").format(CacModVariables.MapVariables.get(world).Pmt_difficulty))), false);
		} else if ((CacModVariables.MapVariables.get(world).Option_tester_str).equals("Decrease Difficulty")) {
			if (CacModVariables.MapVariables.get(world).Pmt_difficulty > 0.3) {
				CacModVariables.MapVariables.get(world).Pmt_difficulty = CacModVariables.MapVariables.get(world).Pmt_difficulty - 0.01;
				CacModVariables.MapVariables.get(world).syncData(world);
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Difficulty : " + new java.text.DecimalFormat("#.##").format(CacModVariables.MapVariables.get(world).Pmt_difficulty))), false);
			} else {
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("But... Difficulty is already minimum!"), false);
			}
		}
	}
}
