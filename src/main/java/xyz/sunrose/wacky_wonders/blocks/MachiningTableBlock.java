package xyz.sunrose.wacky_wonders.blocks;

import com.unascribed.lib39.weld.api.BigBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
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
import org.jetbrains.annotations.Nullable;
import xyz.sunrose.wacky_wonders.items.WackyItems;
import xyz.sunrose.wacky_wonders.util.RotatableVoxelShape;

public class MachiningTableBlock extends BigBlock implements BlockEntityProvider{
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final IntProperty Y = IntProperty.of("y",0,1);

	private static final RotatableVoxelShape CNC_BOX_1 = new RotatableVoxelShape(11, 0, 3, 14, 10, 6);
	private static final RotatableVoxelShape CNC_BOX_2 = new RotatableVoxelShape(11, 0, 6, 14, 8, 14);
	private static final RotatableVoxelShape SANDER_BOX = new RotatableVoxelShape(4, 0, 11, 9, 12, 14);
	private static final RotatableVoxelShape SAW_BOX = new RotatableVoxelShape(1, 0, 6, 3, 3, 14);
	private static final RotatableVoxelShape ITEM_SPOT_BOX = new RotatableVoxelShape(4, 0, 4, 10, 2, 10);


	protected MachiningTableBlock(Settings settings) {
		super(null, Y, null, settings);
		this.setDefaultState(
				this.getStateManager()
						.getDefaultState()
						.with(FACING, Direction.NORTH)
		);
	}

	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.get(Y) == 1) {pos = pos.down();}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (hand == Hand.MAIN_HAND && state.isOf(this) && blockEntity instanceof MachiningTableBlockEntity table) {
			if (player.getStackInHand(hand).getItem() == WackyItems.WRENCH) {
				// if wrench, cycle recipe
				if (table.recipeCount() > 1) {
					table.cycleRecipe();
					return ActionResult.SUCCESS;
				}
				return ActionResult.FAIL;
			} else {
				// otherwise swap items
				ItemStack currentItem = table.getIngredient().copy();
				ItemStack playerHolding = player.getStackInHand(hand).copy();
				if (currentItem.isEmpty() && playerHolding.isEmpty())
					return ActionResult.PASS; //don't swing arm if there's nothing to swap
				table.setIngredient(playerHolding);
				player.setStackInHand(hand, currentItem);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(Y) == 0) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof MachiningTableBlockEntity table) {
				table.dropItem(world, pos, table.getIngredient());
				table.clear();
			}
		}
		super.onBreak(world, pos, state, player);
	}

	// == IMPORTANT BUT SECONDARY STUFF ==
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(Y) == 0 ? VoxelShapes.fullCube() : VoxelShapes.union(
				CNC_BOX_1.getShape(state.get(FACING)),
				CNC_BOX_2.getShape(state.get(FACING)),
				SANDER_BOX.getShape(state.get(FACING)),
				SAW_BOX.getShape(state.get(FACING))
		);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	// == BOILERPLATE STUFF ==
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(Y) == 0 ? new MachiningTableBlockEntity(pos, state) : null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, Y);
	}
}
