package com.thelastflames.skyisles.chunk_generators.surface_builders;

import com.thelastflames.skyisles.chunk_generators.SurfaceBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultSurfaceBuilder extends SurfaceBuilder {
	@Override
	public void generateSurface(World worldIn, int ystart, int x, int z) {
		boolean noblock=true;
		for (int i=ystart;i>=0;i--) {
			BlockPos pos=new BlockPos(x,i,z);
			if (worldIn.getBlockState(pos).isAir(worldIn,pos)) {
				if (!noblock) {
					if (worldIn.getBlockState(pos.up()).equals(Blocks.STONE.getDefaultState())) {
						worldIn.setBlockState(pos.up(),Blocks.WHITE_STAINED_GLASS.getDefaultState(),0);
					}
				}
				noblock=true;
			} else {
				if (noblock) {
					worldIn.setBlockState(pos,Blocks.GRASS_BLOCK.getDefaultState(),0);
				}
				noblock=false;
			}
		}
	}
}
