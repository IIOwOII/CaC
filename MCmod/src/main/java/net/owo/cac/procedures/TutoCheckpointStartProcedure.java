package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;

public class TutoCheckpointStartProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		ListTag pos_checkpoint_initial;
		TimTitlesClearProcedure.execute(entity);
		PrdMeowMoveOnProcedure.execute();
		CacModVariables.Tuto_score_running = true;
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.experience_orb.pickup")), SoundSource.NEUTRAL, 1, 1, false);
			}
		}
		pos_checkpoint_initial = new ListTag();
		pos_checkpoint_initial = (CacModVariables.Tuto_checkpoint_pos.get((CacModVariables.Tuto_checkpoint_route.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
		{
			int _value = 1;
			BlockPos _pos = new BlockPos((pos_checkpoint_initial.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0, (pos_checkpoint_initial.get(1)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0,
					(pos_checkpoint_initial.get(2)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0);
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
	}
}
