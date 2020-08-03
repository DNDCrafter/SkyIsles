package com.thelastflames.skyisles.tile_entity;

import com.thelastflames.skyisles.blocks.ItemObserver;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemObserverTE extends TileEntity {
	public ItemObserverTE(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public ItemStack stack = ItemStack.EMPTY;
	
	public ItemObserverTE() {
		super(SkyTileEntities.ITEM_OBSERVER.get());
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.contains("item")) {
			stack = ItemStack.read(compound.getCompound("item"));
			System.out.println(stack.getOrCreateTag());
			System.out.println(stack);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!stack.isEmpty()) {
			CompoundNBT item = new CompoundNBT();
			CompoundNBT tags = new CompoundNBT();
			if (stack.hasTag()) {
				tags = stack.getTag();
			}
			item.putByte("Count", (byte) stack.getCount());
			item.putString("id", stack.getItem().getRegistryName().toString());
			if (stack.hasTag()) {
				item.put("tag", tags);
			}
			compound.put("item", item);
		}
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.read(nbt);
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		return this.write(new CompoundNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		if (tag.toString().equals("{}")) {
			stack = ItemStack.EMPTY;
		} else {
			this.deserializeNBT(tag);
		}
	}
	
	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		if (this.getBlockState().get(ItemObserver.CLOSED)) {
			CompoundNBT nbt = new CompoundNBT();
			super.write(nbt);
			CompoundNBT item = new CompoundNBT();
			CompoundNBT tags = stack.getOrCreateTag();
			item.putByte("Count", (byte) 0);
			item.putString("id", "minecraft:air");
			item.put("tag", tags);
			nbt.put("item", item);
			return nbt;
		}
		return this.serializeNBT();
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		if (this.getBlockState().get(ItemObserver.CLOSED)) {
			CompoundNBT nbt = new CompoundNBT();
			super.write(nbt);
			CompoundNBT item = new CompoundNBT();
			CompoundNBT tags = stack.getOrCreateTag();
			item.putByte("Count", (byte) 0);
			item.putString("id", "minecraft:air");
			item.put("tag", tags);
			nbt.put("item", item);
			return new SUpdateTileEntityPacket(this.pos, 1, nbt);
		}
		return new SUpdateTileEntityPacket(this.pos, 1, this.serializeNBT());
	}
}
