package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class PrdBuilderUseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 pos_block = Vec3.ZERO;
		pos_block = (new Object() {
			public Vec3 get(Entity entity, double length) {
				return Vec3.atLowerCornerOf(entity.level().clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getLookAngle().scale(length)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null)).getBlockPos());
			}
		}).get(entity, 5);
		if (CacModVariables.MapVariables.get(world).Option_builder == 0) {
			{
				int _value = (int) (((world.getBlockState(BlockPos.containing(pos_block.x(), pos_block.y(), pos_block.z()))).getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip8
						? (world.getBlockState(BlockPos.containing(pos_block.x(), pos_block.y(), pos_block.z()))).getValue(_getip8)
						: -1) + 1);
				BlockPos _pos = BlockPos.containing(pos_block.x(), pos_block.y(), pos_block.z());
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
					world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
			}
		} else if (CacModVariables.MapVariables.get(world).Option_builder == 1) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Pos 1: " + pos_block)), true);
			CacModVariables.MapVariables.get(world).Builder_pos1 = pos_block;
			CacModVariables.MapVariables.get(world).syncData(world);
		} else if (CacModVariables.MapVariables.get(world).Option_builder == 2) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("Pos 2: " + pos_block)), true);
			CacModVariables.MapVariables.get(world).Builder_pos2 = pos_block;
			CacModVariables.MapVariables.get(world).syncData(world);
		}
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		}
	}
}
