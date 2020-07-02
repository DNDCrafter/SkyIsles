package com.thelastflames.skyisles.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolHelper {
	public ItemStack swapForTool(ItemStack stack) {
		ItemStack newStack=stack.copy();
		if (stack.getItem().equals(Items.IRON_INGOT)) {
			newStack=new ItemStack(Items.IRON_PICKAXE);
		} else if (stack.getItem().equals(Items.GOLD_INGOT)) {
			newStack=new ItemStack(Items.GOLDEN_PICKAXE);
		} else if (stack.getItem().equals(Items.DIAMOND)) {
			newStack=new ItemStack(Items.DIAMOND_PICKAXE);
		} else if (stack.getItem().getTags().contains(new ResourceLocation("minecraft:planks"))) {
			newStack=new ItemStack(Items.WOODEN_PICKAXE);
		} else if (stack.getItem().getTags().contains(Tags.Blocks.STONE.getId())||stack.getItem().getTags().contains(Tags.Blocks.COBBLESTONE.getId())) {
			newStack=new ItemStack(Items.STONE_PICKAXE);
		} else if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(stack.getItem().getRegistryName().toString().replace("ingot","pickaxe")))) {
			new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getItem().getRegistryName().toString().replace("ingot","pickaxe"))));
		}
		return newStack;
	}
}
