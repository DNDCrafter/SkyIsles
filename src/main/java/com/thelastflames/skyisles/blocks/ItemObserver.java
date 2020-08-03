package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.tile_entity.ItemObserverTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;


public class ItemObserver extends DirectionalBlock implements ITileEntityProvider {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty CLOSED = BooleanProperty.create("closed");
	
	public ItemObserver(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH).with(POWERED, false));
	}
	
	@Override
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, CLOSED);
	}
	
	@Nonnull
	@Override
	public BlockState rotate(@Nonnull BlockState state, @Nonnull Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@Nonnull
	@Override
	public BlockState mirror(@Nonnull BlockState state, @Nonnull Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
		Direction dir = null;
		if (state.get(FACING).getAxis().isHorizontal()) {
			dir = Direction.UP;
		} else if (state.get(FACING) == Direction.UP) {
			dir = Direction.SOUTH;
		} else if (state.get(FACING) == Direction.DOWN) {
			dir = Direction.NORTH;
		}
		if (hit.getFace() == dir) {
			if (player.getHeldItem(handIn).isEmpty()) {
				worldIn.setBlockState(pos, state.with(CLOSED, !state.get(CLOSED)));
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof ItemObserverTE) {
					worldIn.markChunkDirty(pos, te);
				}
				return ActionResultType.SUCCESS;
			} else if (!state.get(CLOSED)) {
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof ItemObserverTE) {
					((ItemObserverTE) te).stack = player.getHeldItem(handIn).copy();
					worldIn.markChunkDirty(pos, te);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}
	
	@Override
	public void tick(@Nonnull BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
		Direction facing = state.get(FACING);
		TileEntity te1 = worldIn.getTileEntity(pos);
		ItemStack stack1 = (te1 instanceof ItemObserverTE ? ((ItemObserverTE) te1).stack : ItemStack.EMPTY);
		if (!stack1.isEmpty()) {
			TileEntity te = worldIn.getTileEntity(pos.offset(facing));
			if (te instanceof LockableLootTileEntity) {
				LockableLootTileEntity teloot = (LockableLootTileEntity) te;
				for (int i = 0; i < teloot.getSizeInventory(); i++) {
					ItemStack stack = teloot.getStackInSlot(i);
					if (
							stack.isItemEqual(stack1)
									&& stack.getCount() == stack1.getCount()
									&& checkTags(stack, stack1)
					) {
						worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
						worldIn.setBlockState(pos, state.with(POWERED, true));
						updateNeighborsInFront(worldIn, pos, state);
						return;
					}
				}
			} else if (worldIn.getBlockState(pos.offset(facing)).isSolid()) {
				te = worldIn.getTileEntity(pos.offset(facing, 2));
				if (te instanceof LockableLootTileEntity) {
					LockableLootTileEntity teloot = (LockableLootTileEntity) te;
					for (int i = 0; i < teloot.getSizeInventory(); i++) {
						ItemStack stack = teloot.getStackInSlot(i);
						if (
								stack.isItemEqual(stack1)
										&& stack.getCount() == stack1.getCount()
										&& checkTags(stack, stack1)
						) {
							worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
							worldIn.setBlockState(pos, state.with(POWERED, true));
							updateNeighborsInFront(worldIn, pos, state);
							return;
						}
					}
				} else {
					BlockPos pos1 = pos.offset(facing, 2);
					AxisAlignedBB bb = new AxisAlignedBB(pos1);
					List<ItemFrameEntity> ItemFrames = worldIn.getEntitiesWithinAABB(ItemFrameEntity.class, bb);
					for (ItemFrameEntity frame : ItemFrames) {
						System.out.println(Direction.byHorizontalIndex(frame.getRotation()));
						ItemStack stack = frame.getDisplayedItem();
						if (
								stack.isItemEqual(stack1)
										&& stack.getCount() == stack1.getCount()
										&& checkTags(stack, stack1)
						) {
							worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
							worldIn.setBlockState(pos, state.with(POWERED, true));
							updateNeighborsInFront(worldIn, pos, state);
							return;
						}
					}
				}
			} else {
				BlockPos pos1 = pos.offset(facing);
				AxisAlignedBB bb = new AxisAlignedBB(pos1);
				List<ItemFrameEntity> ItemFrames = worldIn.getEntitiesWithinAABB(ItemFrameEntity.class, bb);
				for (ItemFrameEntity frame : ItemFrames) {
					System.out.println(Direction.byHorizontalIndex(frame.getRotation()));
					ItemStack stack = frame.getDisplayedItem();
					if (
							stack.isItemEqual(stack1)
									&& stack.getCount() == stack1.getCount()
									&& checkTags(stack, stack1)
					) {
						worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
						worldIn.setBlockState(pos, state.with(POWERED, true));
						updateNeighborsInFront(worldIn, pos, state);
						return;
					}
				}
			}
		}
		worldIn.setBlockState(pos, state.with(POWERED, false));
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
		
		this.updateNeighborsInFront(worldIn, pos, state);
	}
	
	@Override
	public boolean canConnectRedstone(@Nonnull BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return side == state.get(FACING);
	}
	
	protected void updateNeighborsInFront(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		Direction direction = state.get(FACING);
		BlockPos blockpos = pos.offset(direction.getOpposite());
		worldIn.neighborChanged(blockpos, this, pos);
		worldIn.notifyNeighborsOfStateChange(blockpos, this);
		worldIn.notifyNeighborsOfStateChange(pos, this);
	}
	
	private static boolean checkTags(ItemStack stack, ItemStack stack1) {
		return !stack.hasTag() || (stack1.hasTag() && stack.getTag().equals(stack1.getTag()));
	}
	
	@Override
	public int getWeakPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return blockState.get(POWERED) && side == blockState.get(FACING) ? 15 : 0;
	}
	
	@Override
	public int getStrongPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return getWeakPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public void onBlockAdded(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
		if (state.getBlock() != oldState.getBlock()) {
			if (!worldIn.isRemote() && !worldIn.getPendingBlockTicks().isTickScheduled(pos, this)) {
				BlockState blockstate = state.with(POWERED, false);
				worldIn.setBlockState(pos, blockstate, 18);
				this.updateNeighborsInFront(worldIn, pos, blockstate);
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
			}
		}
	}
	
	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		super.onReplaced(state, worldIn, pos, newState, isMoving);
		if (state.getBlock() != newState.getBlock()) {
			if (!worldIn.isRemote && state.get(POWERED) && worldIn.getPendingBlockTicks().isTickScheduled(pos, this)) {
				this.updateNeighborsInFront(worldIn, pos, state.with(POWERED, false));
			}
		}
	}
	
	@Override
	public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite().getOpposite()).with(POWERED, false).with(CLOSED, false);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ItemObserverTE();
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
		return new ItemObserverTE();
	}
}