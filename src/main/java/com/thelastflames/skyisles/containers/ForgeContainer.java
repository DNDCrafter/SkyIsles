package com.thelastflames.skyisles.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForgeContainer extends Container {
	public static ContainerType<ForgeContainer> TYPE;
	
	final int numSlots;
	
	final CraftingMatrix matrix;
	
	public ForgeContainer(ContainerType<?> type, int id, IInventory playerInventoryIn, IInventory p_i50092_4_, int slots, @Nullable PlayerEntity player) {
		super(TYPE, id);

//		try {
//			playerInventoryIn=Minecraft.getInstance().player.inventory;
//		} catch (Throwable err) {}
		this.numSlots = slots;
		if (player != null) {
			p_i50092_4_.openInventory(player);
		}
		int x = 48;
		int i = numSlots;
		
		IInventory outputInv = new Inventory(1) {
			@Override
			public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
				return false;
			}
		};
		Slot output = new Slot(outputInv, 0, 130, 34) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		};
		
		matrix = new CraftingMatrix(p_i50092_4_, output);
		this.addSlot(output);
		
		int yoff1 = 2;
		
		for (int j = 0; j < 3; ++j) {
			for (int k = 0; k < 3; ++k) {
				this.addSlot(new Slot(p_i50092_4_, k + j * 3, x + k * 18, (18 - yoff1) + j * 18));
			}
		}
		
		int yoff = 19;
		
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(playerInventoryIn, j1 + l * 9 + 9, 8 + j1 * 18, (103 - yoff) + l * 18));
			}
		}
		
		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventoryIn, i1, 8 + i1 * 18, (161 - yoff)));
		}
		
	}
	
	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
		return true;
	}
	
	@Override
	public ContainerType<?> getType() {
		return TYPE;
	}
	
	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Nonnull
	public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
		if (!playerIn.world.isRemote) {
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = this.inventorySlots.get(index);
			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				if (index < this.numSlots) {
					if (!this.mergeItemStack(itemstack1, this.numSlots, this.inventorySlots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, this.numSlots, false)) {
					return ItemStack.EMPTY;
				}
				
				if (itemstack1.isEmpty()) {
					slot.putStack(ItemStack.EMPTY);
				} else {
					slot.onSlotChanged();
				}
			}
			
			return itemstack;
		}
		return ItemStack.EMPTY;
	}
	
	@Nonnull
	@Override
	public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull PlayerEntity player) {
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		updateCrafting();
		return stack;
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}
	
	public void updateCrafting() {
		if (matrix != null) {
			matrix.onChange();
		}
	}
	
	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(@Nonnull PlayerEntity playerIn) {
		if (!playerIn.getEntityWorld().isRemote) {
			super.onContainerClosed(playerIn);
			matrix.onClose(playerIn.world, playerIn.getPosition());
		}
//		InventoryHelper.dropItems(playerIn.world,playerIn.getPosition(),this.getInventory());
	}
	
	public ForgeContainer(int id, PlayerInventory playerInv) {
		this(TYPE, id, playerInv, new com.thelastflames.skyisles.client.gui.Inventory(9), 9, playerInv.player);
	}
}
