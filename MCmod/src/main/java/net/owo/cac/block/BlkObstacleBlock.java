
package net.owo.cac.block;

import org.checkerframework.checker.units.qual.s;

import net.owo.cac.procedures.BlsFenceUpdateProcedure;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class BlkObstacleBlock extends Block {
	public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 6);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public BlkObstacleBlock() {
		super(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1f, 10f).lightLevel(s -> (new Object() {
			public int getLightLevel() {
				if (s.getValue(BLOCKSTATE) == 1)
					return 0;
				if (s.getValue(BLOCKSTATE) == 2)
					return 0;
				if (s.getValue(BLOCKSTATE) == 3)
					return 0;
				if (s.getValue(BLOCKSTATE) == 4)
					return 0;
				if (s.getValue(BLOCKSTATE) == 5)
					return 0;
				if (s.getValue(BLOCKSTATE) == 6)
					return 0;
				return 0;
			}
		}.getLightLevel())).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidstate) {
		return true;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(BLOCKSTATE) == 1) {
			return switch (state.getValue(FACING)) {
				default -> box(7, 0, 7, 9, 24, 9);
				case NORTH -> box(7, 0, 7, 9, 24, 9);
				case EAST -> box(7, 0, 7, 9, 24, 9);
				case WEST -> box(7, 0, 7, 9, 24, 9);
			};
		}
		if (state.getValue(BLOCKSTATE) == 2) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 9, 9, 24, 16));
				case NORTH -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 0, 9, 24, 7));
				case EAST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(9, 0, 7, 16, 24, 9));
				case WEST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(0, 0, 7, 7, 24, 9));
			};
		}
		if (state.getValue(BLOCKSTATE) == 3) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 9, 9, 24, 16), box(0, 0, 7, 7, 24, 9));
				case NORTH -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 0, 9, 24, 7), box(9, 0, 7, 16, 24, 9));
				case EAST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(9, 0, 7, 16, 24, 9), box(7, 0, 9, 9, 24, 16));
				case WEST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(0, 0, 7, 7, 24, 9), box(7, 0, 0, 9, 24, 7));
			};
		}
		if (state.getValue(BLOCKSTATE) == 4) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 9, 9, 24, 16), box(7, 0, 0, 9, 24, 7));
				case NORTH -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 0, 9, 24, 7), box(7, 0, 9, 9, 24, 16));
				case EAST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(9, 0, 7, 16, 24, 9), box(0, 0, 7, 7, 24, 9));
				case WEST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(0, 0, 7, 7, 24, 9), box(9, 0, 7, 16, 24, 9));
			};
		}
		if (state.getValue(BLOCKSTATE) == 5) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 9, 9, 24, 16), box(0, 0, 7, 7, 24, 9), box(7, 6, 0, 9, 7, 7));
				case NORTH -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 0, 9, 24, 7), box(9, 0, 7, 16, 24, 9), box(7, 6, 9, 9, 7, 16));
				case EAST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(9, 0, 7, 16, 24, 9), box(7, 0, 9, 9, 24, 16), box(0, 6, 7, 7, 7, 9));
				case WEST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(0, 0, 7, 7, 24, 9), box(7, 0, 0, 9, 24, 7), box(9, 6, 7, 16, 7, 9));
			};
		}
		if (state.getValue(BLOCKSTATE) == 6) {
			return switch (state.getValue(FACING)) {
				default -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 9, 9, 24, 16), box(0, 0, 7, 7, 24, 9), box(7, 6, 0, 9, 7, 7), box(9, 6, 7, 16, 7, 9));
				case NORTH -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(7, 0, 0, 9, 24, 7), box(9, 0, 7, 16, 24, 9), box(7, 6, 9, 9, 7, 16), box(0, 6, 7, 7, 7, 9));
				case EAST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(9, 0, 7, 16, 24, 9), box(7, 0, 9, 9, 24, 16), box(0, 6, 7, 7, 7, 9), box(7, 6, 0, 9, 7, 7));
				case WEST -> Shapes.or(box(7, 0, 7, 9, 24, 9), box(0, 0, 7, 7, 24, 9), box(7, 0, 0, 9, 24, 7), box(9, 6, 7, 16, 7, 9), box(7, 6, 9, 9, 7, 16));
			};
		}
		return switch (state.getValue(FACING)) {
			default -> box(7, 0, 7, 9, 24, 9);
			case NORTH -> box(7, 0, 7, 9, 24, 9);
			case EAST -> box(7, 0, 7, 9, 24, 9);
			case WEST -> box(7, 0, 7, 9, 24, 9);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, BLOCKSTATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
		return BlockPathTypes.FENCE;
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		BlsFenceUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
		super.neighborChanged(blockstate, world, pos, neighborBlock, fromPos, moving);
		BlsFenceUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}
