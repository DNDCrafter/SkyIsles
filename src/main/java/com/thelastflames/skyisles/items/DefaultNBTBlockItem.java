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

public class DefaultNBTBlockItem extends BlockItem {
	private CompoundNBT defaultNBT;
	public DefaultNBTBlockItem(Block blockIn, Properties builder, CompoundNBT nbt) {
		super(blockIn, builder);
		this.defaultNBT=nbt;
	}
	
	public CompoundNBT getDefaultNBT() {
		return defaultNBT;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		return super.onItemUse(context);
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context) {
		return super.tryPlace(context);
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		return super.placeBlock(context, state);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.hasTag()) {
			stack.setTag(defaultNBT);
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}
