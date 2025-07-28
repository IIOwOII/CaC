package net.owo.cac.procedures;

import net.owo.cac.network.CacModVariables;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;

public class TutoCheckpointProcedure {
	public static void execute(LevelAccessor world) {
		ListTag pos_checkpoint_initial;
		if (!world.isClientSide() && world.getServer() != null)
			world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("go!"), false);
		pos_checkpoint_initial = new ListTag();
		pos_checkpoint_initial = (CacModVariables.MapVariables.get(world).Tuto_checkpoint_pos.get((CacModVariables.MapVariables.get(world).Tuto_checkpoint_route.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)) instanceof ListTag _listTag
				? _listTag.copy()
				: new ListTag();
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
