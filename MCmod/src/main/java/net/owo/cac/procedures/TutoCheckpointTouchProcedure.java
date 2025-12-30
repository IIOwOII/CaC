package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;

public class TutoCheckpointTouchProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ListTag pos_checkpoint;
		double idx_checkpoint = 0;
		CacModVariables.Tuto_checkpoint_index = CacModVariables.Tuto_checkpoint_index + 1;
		if (CacModVariables.Tuto_checkpoint_index < CacModVariables.Tuto_checkpoint_route.size()) {
			idx_checkpoint = (CacModVariables.Tuto_checkpoint_route.get((int) CacModVariables.Tuto_checkpoint_index)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0;
			if (idx_checkpoint == -1) {
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(CacModVariables.Tuto_checkpoint_center.x(), CacModVariables.Tuto_checkpoint_center.y(), CacModVariables.Tuto_checkpoint_center.z());
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			} else {
				pos_checkpoint = new ListTag();
				pos_checkpoint = (CacModVariables.Tuto_checkpoint_pos.get((int) idx_checkpoint)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag();
				{
					int _value = 1;
					BlockPos _pos = new BlockPos((pos_checkpoint.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0, (pos_checkpoint.get(1)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0,
							(pos_checkpoint.get(2)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			}
		} else {
			TutoCheckpointEndProcedure.execute(world, x, y, z);
		}
	}
}
