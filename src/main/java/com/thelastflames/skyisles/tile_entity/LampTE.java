package com.thelastflames.skyisles.tile_entity;

import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class LampTE extends MultiMaterialTE {
	public String light="minecraft:glowstone";
	
	public LampTE() {
		this(SkyTileEntities.LAMPTE.get());
	}
	
	public LampTE(TileEntityType type) {
		super(type);
	}
	
	@Override
	public void read(@Nonnull CompoundNBT compound) {
		super.read(compound);
		this.light=compound.getString("light");
	}
	
	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound) {
		super.write(compound);
		compound.putString("light",this.light);
		return compound;
	}
}
