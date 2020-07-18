package com.thelastflames.skyisles.island_structures;

import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.utils.random.Random1;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

public class SimpleTree extends Structure {
	@Override
	public void generate(int x, int y, int z, IWorld world) {
		long l=new BlockPos(x,y,z).toLong()*world.getSeed();
		Random1.setSeed(l);
		int height=Random1.getIntWithLimit(4,true)+4;
		for (int i=0;i<height;i++) {
			generateLeave(x,y+i+1,z,world);
			generateLog(x,y+i,z,world);
			if (i>=height-4) {
				generateLeave(x+1,y+i+1,z,world);
				generateLeave(x-1,y+i+1,z,world);
				generateLeave(x,y+i+1,z+1,world);
				generateLeave(x,y+i+1,z-1,world);
				generateLeave(x,y+i+1,z,world);
				if (i>=height-3&&i<=height-2) {
					for (int xLeaf=-2;xLeaf<=2;xLeaf++) {
						for (int zLeaf=-2;zLeaf<=2;zLeaf++) {
							generateLeave(x+xLeaf,y+i,z+zLeaf,world);
						}
					}
				}
			}
		}
	}
	
	public void generateLeave(int x, int y, int z, IWorld world) {
		if (world.getWorld().getBlockState(new BlockPos(x,y,z)).canBeReplacedByLeaves(world,new BlockPos(x,y,z))) {
			world.getWorld().setBlockState(new BlockPos(x,y,z), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE,1));
		}
	}
	
	public void generateLog(int x, int y, int z, IWorld world) {
		if (world.getWorld().getBlockState(new BlockPos(x,y,z)).canBeReplacedByLogs(world,new BlockPos(x,y,z))) {
			world.getWorld().setBlockState(new BlockPos(x,y,z), Blocks.OAK_LOG.getDefaultState());
		}
	}
	
	@Override
	public void spawn(int x, int y, int z, IChunk chunk) {
		chunk.setBlockState(new BlockPos(x,y,z), SkyBlocks.TREE_SPAWNER_BLOCK.getObject1().get().getDefaultState(), false);
		chunk.getBlocksToBeTicked().scheduleTick(chunk.getPos().asBlockPos().add(new BlockPos(x,y,z)), SkyBlocks.TREE_SPAWNER_BLOCK.getObject1().get(),1);
	}
}
