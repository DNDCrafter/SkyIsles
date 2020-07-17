package com.thelastflames.skyisles.containers;

import com.thelastflames.skyisles.API.utils.ForgeRecipeRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CraftingMatrix {
	final IInventory inventory;
	
	final Slot output;
	
	public CraftingMatrix(IInventory inventory, Slot outputSlot) {
		output=outputSlot;
		this.inventory=inventory;
	}
	
	public void onChange() {
		ArrayList<ItemStack> stacks=new ArrayList<>();
		try {
			for (int i=0;i<inventory.getSizeInventory();i++) {
				stacks.add(inventory.getStackInSlot(i));
			}
		} catch (Throwable err) {}
		output.putStack(ItemStack.EMPTY);
		ForgeRecipeRegistry.forEach((l, r)->{
			if (r.checkMatch(stacks)) {
				output.putStack(r.getOutput(stacks));
			}
		});
	}
	
	public void onClose(World world, BlockPos pos) {
		ArrayList<ItemStack> stacks=new ArrayList<>();
		for (int i=0;i<=inventory.getSizeInventory();i++) {
			stacks.add(inventory.getStackInSlot(i));
		}
		stacks.forEach((s)->InventoryHelper.spawnItemStack(world,pos.getX(),pos.getY(),pos.getZ(),s.copy()));
	}
}
