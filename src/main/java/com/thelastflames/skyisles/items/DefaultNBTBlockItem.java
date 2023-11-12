package com.thelastflames.skyisles.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public class DefaultNBTBlockItem extends BlockItem {
	private final CompoundTag defaultNBT;

	public DefaultNBTBlockItem(Block blockIn, Properties builder, CompoundTag nbt) {
		super(blockIn, builder);
		this.defaultNBT = nbt;
	}

	public CompoundTag getDefaultNBT() {
		return defaultNBT;
	}

    @Override
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
		if (!stack.hasTag()) {
			stack.setTag(defaultNBT);
		}
        super.inventoryTick(stack, p_41405_, p_41406_, p_41407_, p_41408_);
	}
}
