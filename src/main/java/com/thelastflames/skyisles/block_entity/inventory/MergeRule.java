package com.thelastflames.skyisles.block_entity.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface MergeRule {
    boolean canMerge(
            BlockPos pos,
            Direction dir,
            BlockState state
    );
}
