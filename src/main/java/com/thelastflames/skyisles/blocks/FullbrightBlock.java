package com.thelastflames.skyisles.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class FullbrightBlock extends Block {
	public FullbrightBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1f;
	}
	
	@Override
	public int getLightValue(BlockState state) {
		return 0;
	}
}
