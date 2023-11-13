package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.block_entity.ContainerBE;
import com.thelastflames.skyisles.block_entity.inventory.ContainerInventory;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.StringyHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class DrawerBlock extends Block implements EntityBlock {
    public DrawerBlock() {
        super(Block.Properties.copy(Blocks.CHEST));
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
                    return ((ContainerBE) tileentity).openGui(pContainerId, pPlayerInventory, 0, pPlayer);
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
                tileentity.setRemoved();
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
}
