package com.thelastflames.skyisles.Blocks;

import com.thelastflames.skyisles.Registry.SkyTileEntities;
import com.thelastflames.skyisles.Utils.MaterialList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiMaterialTE extends TileEntity {
	public MaterialList materialList=new MaterialList();
	
	public MultiMaterialTE() {
		super(SkyTileEntities.MULTIMATERIALTE.get());
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (!compound.contains("x")) {
			try {
				this.pos=new BlockPos(0,-9999,0);
			} catch (Exception err) {}
		}
//		System.out.println(compound.getString("materials"));
		materialList=MaterialList.fromString(compound.getString("materials"));
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
