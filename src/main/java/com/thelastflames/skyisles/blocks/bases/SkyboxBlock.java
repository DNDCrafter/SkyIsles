package com.thelastflames.skyisles.blocks.bases;

import com.thelastflames.skyisles.blocks.FullbrightBlock;
import com.thelastflames.skyisles.tile_entity.SkyboxTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkyboxBlock extends FullbrightBlock implements ITileEntityProvider {
	public SkyboxBlock(Properties properties) {
		super(properties);
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SkyboxTileEntity();
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
		return new SkyboxTileEntity();
	}
	
	public ResourceLocation getBackground(World world) {
		return null;
	}
	public ResourceLocation getSky(World world) {
		return null;
	}
	public ResourceLocation getStars(World world) {
		return null;
	}
}
