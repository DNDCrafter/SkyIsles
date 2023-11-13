package com.thelastflames.skyisles.block_entity.inventory;

import net.minecraft.core.Direction;

public interface UISizer {
    void calculateSize(
            int[] size, Direction[] directions
    );
}
