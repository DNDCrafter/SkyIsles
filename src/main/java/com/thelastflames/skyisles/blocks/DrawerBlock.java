package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.block_entity.ContainerBE;
import com.thelastflames.skyisles.block_entity.inventory.ContainerInventory;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.shape.ShapeRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DrawerBlock extends Block implements EntityBlock {
    public DrawerBlock() {
        super(Block.Properties.copy(Blocks.CHEST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FurnaceBlock.FACING);
    }

    private static final VoxelShape[] TOPS = ShapeRotator.steps(
            Block.box(1, 9, 0, 15, 15, 15),
            ShapeRotator.Transformation.RIGHT, ShapeRotator.Transformation.RIGHT, ShapeRotator.Transformation.RIGHT
    );

    private static final VoxelShape[] BOTTOMS = ShapeRotator.steps(
            Block.box(1, 1, 0, 15, 7, 15),
            ShapeRotator.Transformation.RIGHT, ShapeRotator.Transformation.RIGHT, ShapeRotator.Transformation.RIGHT
    );

    protected int getShapeIndex(BlockState state, int part) {
        Direction dir = state.getValue(FurnaceBlock.FACING);
        switch (part) {
            case 0: // top drawer
            case 1: // bottom drawer
                return switch (dir) {
                    case NORTH -> 0;
                    case EAST -> 1;
                    case SOUTH -> 2;
                    case WEST -> 3;
                    default -> throw new RuntimeException("4D drawer???");
                };
            case 2: // base
                return 0;
        }
        return -1;
    }

    protected int findPartIndex(Entity entity, BlockState pState, BlockPos pPos) {
        VoxelShape[] shapes = new VoxelShape[]{
                TOPS[getShapeIndex(pState, 0)], BOTTOMS[getShapeIndex(pState, 1)], Shapes.block()
        };

        double best = Double.POSITIVE_INFINITY;
        int index = 2;

        Vec3 start = entity.getEyePosition();
        double scl = pPos.distToCenterSqr(start) + 1;
        Vec3 end = start.add(entity.getLookAngle().multiply(scl, scl, scl));
        for (int i = 0; i < shapes.length; i++) {
            BlockHitResult bhr = shapes[i].clip(
                    start,
                    end,
                    pPos
            );
            if (bhr != null) {
                double dist = bhr.getLocation().distanceTo(start);
                if (dist < best) {
                    if (Math.abs(dist - best) < 0.01) continue;

                    index = i;
                    best = dist;
                }
            }
        }

        return index;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape[] shapes = new VoxelShape[]{
                TOPS[getShapeIndex(pState, 0)], BOTTOMS[getShapeIndex(pState, 1)], Shapes.block()
        };
        if (pContext instanceof EntityCollisionContext ctx && ctx.getEntity() != null)
            return shapes[findPartIndex(ctx.getEntity(), pState, pPos)];
        return Shapes.block();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (menuprovider != null) {
                pPlayer.openMenu(menuprovider);
//                pPlayer.awardStat(this.getOpenChestStat());
                PiglinAi.angerNearbyPiglins(pPlayer, true);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.empty(); // TODO
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                BlockEntity tileentity = pLevel.getBlockEntity(pPos);
                if (tileentity instanceof ContainerBE) {
                    int id = findPartIndex(
                            pPlayer, pState, pPos
                    );
                    if (id != 2)
                        return ((ContainerBE) tileentity).openGui(pContainerId, pPlayerInventory, id, pPlayer);
                }
                return null;
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = level.getBlockEntity(pos);
            if (tileentity instanceof ContainerBE) {
                ((ContainerBE) tileentity).removed();
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public static ContainerBE createBE(BlockPos blockPos, BlockState state) {
        return new ContainerBE(
                SkyTileEntities.DRAWER_TE.get(),
                blockPos, state,
                new ContainerInventory[]{
                        // top
                        new ContainerInventory(
                                1, 9,
                                (pos, dir, selfState) -> false,
                                (size, directions) -> {
                                    size[0] = 1;
                                    size[1] = 9;
                                }
                        ),
                        // bottom
                        new ContainerInventory(
                                1, 9,
                                (pos, dir, selfState) -> false,
                                (size, directions) -> {
                                    size[0] = 1;
                                    size[1] = 9;
                                }
                        )
                }
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return createBE(pPos, pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return Shapes.block();
    }
}
