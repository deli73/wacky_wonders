package xyz.sunrose.wacky_wonders.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import xyz.sunrose.wacky_wonders.util.RotatableVoxelShape;

public class MachiningTableBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

	private static final RotatableVoxelShape CNC_BOX_1 = new RotatableVoxelShape(11, 0, 3, 14, 10, 6);
	private static final RotatableVoxelShape CNC_BOX_2 = new RotatableVoxelShape(11, 0, 6, 14, 8, 14);
	private static final RotatableVoxelShape SANDER_BOX = new RotatableVoxelShape(4, 0, 11, 9, 12, 14);
	private static final RotatableVoxelShape SAW_BOX = new RotatableVoxelShape(1, 0, 6, 3, 3, 14);
	private static final RotatableVoxelShape ITEM_SPOT_BOX = new RotatableVoxelShape(4, 0, 4, 10, 2, 10);

	protected MachiningTableBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
				this.getStateManager()
						.getDefaultState()
						.with(FACING, Direction.NORTH)
						.with(HALF, DoubleBlockHalf.LOWER)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockPos entityPos = state.get(HALF) == DoubleBlockHalf.UPPER ? pos : pos.up();
		BlockEntity blockEntity = world.getBlockEntity(entityPos);
		if (state.isOf(this) && blockEntity instanceof MachiningTableBlockEntity table) {
			// if wrench, cycle recipe
			table.cycleRecipe();

			// otherwise swap items
			ItemStack currentItem = table.getIngredient().copy();
			ItemStack playerHolding = player.getStackInHand(hand).copy();
			if (currentItem.isEmpty() && playerHolding.isEmpty()) return ActionResult.PASS; //don't swing arm if there's nothing to swap
			table.setIngredient(playerHolding);
			player.setStackInHand(hand, currentItem);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())){
			if (state.get(HALF) == DoubleBlockHalf.UPPER){
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (state.isOf(this) && blockEntity instanceof MachiningTableBlockEntity table) {
					table.dropItem(world, pos, table.getIngredient());
				}
			}
		}
	}


	// == IMPORTANT BUT SECONDARY STUFF, MOSTLY BORROWED FROM DOOR CODE ==
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? VoxelShapes.fullCube() : VoxelShapes.union(
				CNC_BOX_1.getShape(state.get(FACING)),
				CNC_BOX_2.getShape(state.get(FACING)),
				SANDER_BOX.getShape(state.get(FACING)),
				SAW_BOX.getShape(state.get(FACING))
		);
	}

	public BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		DoubleBlockHalf half = state.get(HALF);
		if (direction.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP)) {
			return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)
					? Blocks.AIR.getDefaultState()
					: super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			return neighborState.isOf(this) && neighborState.get(HALF) != half
					? state.with(FACING, neighborState.get(FACING))
					: Blocks.AIR.getDefaultState();
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		World world = ctx.getWorld();
		if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
			return this.getDefaultState()
					.with(FACING, ctx.getPlayerFacing().getOpposite())
					.with(HALF, DoubleBlockHalf.LOWER);
		} else {
			return null;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), NOTIFY_NEIGHBORS | NOTIFY_LISTENERS);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && player.isCreative()) {
			DoubleBlockHalf doubleBlockHalf = state.get(HALF);
			if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
				BlockPos below = pos.down();
				BlockState belowState = world.getBlockState(below);
				if (belowState.isOf(state.getBlock()) && belowState.get(HALF) == DoubleBlockHalf.LOWER) {
					world.setBlockState(below, Blocks.AIR.getDefaultState(), NOTIFY_NEIGHBORS | NOTIFY_LISTENERS | SKIP_DROPS);
					world.syncWorldEvent(player, 2001, below, Block.getRawIdFromState(belowState));
				}
			}
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos below = pos.down();
		BlockState belowState = world.getBlockState(below);
		return state.get(HALF) == DoubleBlockHalf.LOWER || belowState.isOf(this);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.IGNORE;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	// == BOILERPLATE STUFF ==
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.UPPER ? new MachiningTableBlockEntity(pos, state) : null;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}
}
