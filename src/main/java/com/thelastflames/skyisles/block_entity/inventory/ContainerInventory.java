package com.thelastflames.skyisles.block_entity.inventory;

import com.thelastflames.skyisles.block_entity.ContainerBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContainerInventory implements Container {
    final int rows;
    final int columns;
    final MergeRule rule;
    final UISizer sizer;

    final List<ItemStack> stacks;
    int items = 0;

    final boolean storeAsByte;

    ContainerBE owner;

    public ContainerInventory(int rows, int columns, MergeRule rule, UISizer sizer) {
        this.rows = rows;
        this.columns = columns;
        this.stacks = new ArrayList<>(rows * columns);
        this.rule = rule;
        this.sizer = sizer;

        storeAsByte = rows * columns <= 255;

        while (stacks.size() < rows * columns) stacks.add(null);
    }

    public void load(ListTag tag) {
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag tg = tag.getCompound(i);

            int slot;
            if (storeAsByte) slot = tg.getByte("Slot");
            else slot = tg.getShort("Slot");

            stacks.set(slot, ItemStack.of(tg));
        }
    }

    public Tag serialize() {
        ListTag allItems = new ListTag();
        for (int i = 0; i < this.stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack != null && !stack.isEmpty()) {
                CompoundTag iTag = stack.save(new CompoundTag());

                if (storeAsByte) iTag.putByte("Slot", (byte) i);
                else iTag.putShort("Slot", (short) i);

                allItems.add(iTag);
            }
        }
        return allItems;
    }

    public AbstractContainerMenu open(int container, Inventory playerInv, Player pPlayer) {
        if (columns == 9) {
            switch (rows) {
                case 1:
                    return new ChestMenu(MenuType.GENERIC_9x1, container, playerInv, this, 1);
                case 2:
                    return new ChestMenu(MenuType.GENERIC_9x2, container, playerInv, this, 2);
                case 3:
                    return new ChestMenu(MenuType.GENERIC_9x3, container, playerInv, this, 3);
                case 4:
                    return new ChestMenu(MenuType.GENERIC_9x4, container, playerInv, this, 4);
                case 5:
                    return new ChestMenu(MenuType.GENERIC_9x5, container, playerInv, this, 5);
                case 6:
                    return new ChestMenu(MenuType.GENERIC_9x6, container, playerInv, this, 6);
            }
        }

        if (columns == 5 && rows == 1)
            return new HopperMenu(container, playerInv, this);

        throw new RuntimeException("NYI size: " + rows + ", " + columns);
    }

    @Override
    public int getContainerSize() {
        return rows * columns;
    }

    @Override
    public boolean isEmpty() {
        return items == 0;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        ItemStack stack = stacks.get(pSlot);
        if (stack == null) return ItemStack.EMPTY;
        return stack;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack stack = stacks.get(pSlot);
        if (stack == null) return ItemStack.EMPTY;

        ItemStack copy = stack.copy();
        stack.shrink(pAmount);
        if (stack.isEmpty()) {
            items--;
            this.stacks.set(pSlot, null);
        }
        setChanged();
        return copy;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = stacks.remove(pSlot);
        if (stack == null) return ItemStack.EMPTY;
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (stacks.set(pSlot, pStack) == null) {
            items++;
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        owner.setChanged();
        owner.getLevel().markAndNotifyBlock(
                owner.getBlockPos(),
                null, owner.getBlockState(),
                owner.getBlockState(),
                0, 0
        );
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true; // TODO:
    }

    @Override
    public void clearContent() {
        items = 0;
        Collections.fill(stacks, null);
        setChanged();
    }

    public void setOwner(ContainerBE containerBE) {
        this.owner = containerBE;
    }

    public void drop(Level level) {

    }
}
