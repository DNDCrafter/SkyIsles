package com.thelastflames.skyisles.tile_entity;

import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.MaterialList;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiMaterialTE extends TileEntity implements IMultiMaterialTE {
	public MaterialList materialList=new MaterialList();
	
	@Override
	public void read(CompoundNBT nbt, ItemStack stack, BlockState state) {
		read(nbt);
	}
	
	public MultiMaterialTE() {
		super(SkyTileEntities.MULTIMATERIALTE.get());
	}
	
	public MultiMaterialTE(TileEntityType type) {
		super(type);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (!compound.contains("x")) {
			try {
				this.pos=new BlockPos(0,-9999,0);
			} catch (Exception ignored) {}
		}
//		System.out.println(compound.getString("materials"));
		materialList=MaterialList.fromString(compound.getString("materials"));
	}
	
	@Override
	public MaterialList getMaterialList() {
		return materialList;
	}
	
	@Override
	public boolean hasFastRenderer() {
		return true;
	}
	
	@Override
	public BlockState getBlockState() {
		return this.getWorld().getBlockState(this.getPos());
	}
	
	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString("materials",materialList.toString());
		return compound;
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		read(tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		this.read(pkt.getNbtCompound());
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.write(new CompoundNBT()));
	}
	
	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}
}
