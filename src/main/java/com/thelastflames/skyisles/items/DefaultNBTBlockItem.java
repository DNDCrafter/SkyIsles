package com.thelastflames.skyisles.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DefaultNBTBlockItem extends BlockItem {
	private final CompoundNBT defaultNBT;
	public DefaultNBTBlockItem(Block blockIn, Properties builder, CompoundNBT nbt) {
		super(blockIn, builder);
		this.defaultNBT=nbt;
	}
	
	public CompoundNBT getDefaultNBT() {
		return defaultNBT;
	}
	
	@Nonnull
	@Override
	public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
		return super.onItemUse(context);
	}
	
	@Nonnull
	@Override
	public ActionResultType tryPlace(@Nonnull BlockItemUseContext context) {
		return super.tryPlace(context);
	}
	
	@Override
	protected boolean placeBlock(@Nonnull BlockItemUseContext context, @Nonnull BlockState state) {
		return super.placeBlock(context, state);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.hasTag()) {
			stack.setTag(defaultNBT);
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}
