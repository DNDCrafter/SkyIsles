package com.thelastflames.skyisles.block_entity;

import com.thelastflames.skyisles.block_entity.inventory.ContainerInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ContainerBE extends BlockEntity {
    ContainerInventory[] inventories;

    public ContainerBE(
            BlockEntityType<?> type,
            BlockPos p_155331_, BlockState p_155332_,
            ContainerInventory[] inventories
    ) {
        super(type, p_155331_, p_155332_);
        this.inventories = inventories;
        for (ContainerInventory inventory : inventories) {
            inventory.setOwner(this);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ListTag inventories = new ListTag();
        for (ContainerInventory inventory : this.inventories)
            inventories.add(inventory.serialize());
        pTag.put("Inventories", inventories);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ListTag tag = pTag.getList("Inventories", CompoundTag.TAG_LIST);
        for (int i = 0; i < tag.size(); i++)
            inventories[i].load((ListTag) tag.get(i));
    }

    public AbstractContainerMenu openGui(int container, Inventory playerInv, int inventory, Player pPlayer) {
        return inventories[inventory].open(container, playerInv, pPlayer);
    }

    public void removed() {
        for (ContainerInventory inventory : inventories) {
            Containers.dropContents(level, worldPosition, inventory);
        }
    }
}
