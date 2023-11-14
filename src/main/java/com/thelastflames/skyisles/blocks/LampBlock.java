package com.thelastflames.skyisles.blocks;

import com.thelastflames.skyisles.block_entity.LampBE;
import com.thelastflames.skyisles.blocks.bases.DynamicModelBlock;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LampBlock extends DynamicModelBlock {
    public LampBlock() {
        super(Block.Properties.copy(Blocks.CHEST));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LampBE(pPos, pState);
    }
}
