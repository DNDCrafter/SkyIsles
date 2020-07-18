package com.thelastflames.skyisles.chunk_generators;

import com.thelastflames.skyisles.biomes.BiomeBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

import javax.annotation.Nonnull;

public class TestGenerator extends ChunkGenerator<GenerationSettings> {
	public TestGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, GenerationSettings generationSettingsIn) {
		super(worldIn, biomeProviderIn, generationSettingsIn);
	}
	
	@Override
	public void generateSurface(@Nonnull WorldGenRegion p_225551_1_, @Nonnull IChunk p_225551_2_) {
	}
	
	private TweakedEndGenerator generator=null;
	
	@Override
	public int getGroundHeight() {
		return 0;
	}
	
	@Override
	public void makeBase(@Nonnull IWorld worldIn, @Nonnull IChunk chunkIn) {
//		for (int x=0;x<=15;x++) {
//			for (int z=0;z<=15;z++) {
//				if (new Random(chunkIn.getPos().asLong()*worldIn.getSeed()).nextDouble()>=new Random(chunkIn.getPos().asLong()).nextDouble()) {
//					Random r=new Random(worldIn.getSeed()*chunkIn.getPos().asBlockPos().add(x,128,z).toLong());
//					if (r.nextInt(16)>=6) {
//						if (r.nextBoolean()) {
//							worldIn.setBlockState(chunkIn.getPos().asBlockPos().add(x,128+r.nextInt(8),z), Blocks.STONE.getDefaultState(), 0);
//						}
//					}
//				}
//			}
//		}
//		for (int x=0;x<=15;x++) {
//			for (int z=0;z<=15;z++) {
//				for (int y=0;y<=8;y++) {
//					for (int x2=0;x2<=15;x2++) {
//						for (int z2=0;z2<=15;z2++) {
//							BlockPos pos1 = new BlockPos(x,128+y,z);
//							BlockPos pos2 = new BlockPos(x2,128+y,z2);
//							if (pos1.distanceSq(pos2)<=3) {
//								try {
//									if (worldIn.getBlockState(chunkIn.getPos().asBlockPos().add(pos2)).equals(Blocks.STONE.getDefaultState())) {
//										worldIn.setBlockState(chunkIn.getPos().asBlockPos().add(pos1),Blocks.STONE.getDefaultState(),0);
//									}
//								} catch (Exception err) {}
//							}
//						}
//					}
//				}
//			}
//		}

//				int numSpots=8;
//				BlockPos[] topPoses=new BlockPos[numSpots];
//				BlockPos[] botPoses=new BlockPos[numSpots];
//				Random r=new Random(chunkIn.getPos().asLong()*chunkIn.getPos().asBlockPos().toLong());
//				int x=-999;
//				int y=-999;
//				int number=0;
//				for (int i:r.ints(numSpots*3,0,15).toArray()) {
//					if (x==-999) {
//						x=i;
//					} else if (y==-999) {
//						y=i/3;
//						y+=128;
//					} else {
//						worldIn.setBlockState(chunkIn.getPos().asBlockPos().add(x,y,i),Blocks.STONE.getDefaultState(),0);
//						topPoses[number]=chunkIn.getPos().asBlockPos().add(x,y,i);
//						number++;
//						x=-999;
//						y=-999;
//					}
//				}
//				x=-999;
//				y=-999;
//				number=0;
//				for (int i:r.ints(numSpots*3,0,15).toArray()) {
//					if (x==-999) {
//						x=i;
//					} else if (y==-999) {
//						y=-i/3;
//						y+=128;
//					} else {
//						worldIn.setBlockState(chunkIn.getPos().asBlockPos().add(x,y,i),Blocks.OBSIDIAN.getDefaultState(),0);
//						botPoses[number]=chunkIn.getPos().asBlockPos().add(x,y,i);
//						number++;
//						x=-999;
//						y=-999;
//					}
//				}
//				try {
//					for (int i=0;i<topPoses.length;i++) {
//						int startX=topPoses[i].getX();
//						int startX2=botPoses[i].getX();
//						int endX=topPoses[i+1].getX();
//						int endX2=botPoses[i+1].getX();
//						int startY=topPoses[i].getY();
//						int startY2=botPoses[i].getY();
//						int endY=topPoses[i+1].getY();
//						int endY2=botPoses[i+1].getY();
//						int startZ=topPoses[i].getZ();
//						int startZ2=botPoses[i].getZ();
//						int endZ=topPoses[i+1].getZ();
//						int endZ2=botPoses[i+1].getZ();
//						for (int h=0;h<=16;h++) {
//							int placeX=(int)MathHelper.lerp(h/16f,startX,endX);
//							int placeY=(int)MathHelper.lerp(h/16f,startY,endY);
//							int placeZ=(int)MathHelper.lerp(h/16f,startZ,endZ);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.GLASS.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX,endX2);
//							placeY=(int)MathHelper.lerp(h/16f,startY,endY2);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ,endZ2);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.GLASS.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX2,endX2);
//							placeY=(int)MathHelper.lerp(h/16f,startY2,endY2);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ2,endZ2);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.COARSE_DIRT.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX2,endX);
//							placeY=(int)MathHelper.lerp(h/16f,startY2,endY);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ2,endZ);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.RED_WOOL.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX2,endX2);
//							placeY=(int)MathHelper.lerp(h/16f,startY,endY);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ2,endZ2);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.BLUE_WOOL.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX,endX2);
//							placeY=(int)MathHelper.lerp(h/16f,startY2,endY);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ,endZ2);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.GREEN_WOOL.getDefaultState(),0);
//							}
//							placeX=(int)MathHelper.lerp(h/16f,startX2,endX2);
//							placeY=(int)MathHelper.lerp(h/16f,startY,endY);
//							placeZ=(int)MathHelper.lerp(h/16f,startZ,endZ2);
//							if (worldIn.getBlockState(new BlockPos(placeX,placeY,placeZ)).isAir()) {
//								worldIn.setBlockState(new BlockPos(placeX,placeY,placeZ),Blocks.LIME_WOOL.getDefaultState(),0);
//							}
//						}
//					}
//				} catch (Exception err) {}
		
//		Random r=new Random(chunkIn.getPos().asBlockPos().getX()%1600);
//		Random r2=new Random(chunkIn.getPos().asBlockPos().getX()%160);
//		Random r3=new Random(chunkIn.getPos().asBlockPos().getZ()%160);
//		Random r4=new Random((r.nextInt(16)*chunkIn.getPos().asBlockPos().getZ()%160));
//		Random r5=new Random((r4.nextInt(16)*chunkIn.getPos().asBlockPos().getX()%16040));
//		Random r6=new Random((int)(((r.nextLong()*r2.nextInt(5)/r3.nextInt(7))%r4.nextInt(5325))/r5.nextDouble()));
//
//		Random r7=new Random(r6.nextLong()*worldIn.getSeed());
		
//		ArrayList<Integer> ints=new ArrayList<>();
//		for (int x=0;x<=16;x++) {
//			for (int z=0;z<=16;z++) {
//				ints.add(r7.nextInt(16));
//			}
//		}
		
		if (generator==null) {
			generator=new TweakedEndGenerator(worldIn,this.biomeProvider,new EndGenerationSettings());
		}
		generator.makeBase(worldIn,chunkIn);
		
		for (int x=16;x>=0;x--) {
			for (int z=16;z>=0;z--) {
//				int f = 0;
//				int gi = 0;
//				for (int i=0;i<ints.size();i++) {
//					if (ints.get(i)>f) {
//						f=ints.get(i);
//						gi=i;
//					}
//				}
//				ints.remove(gi);
				
				BlockState topBlock=Blocks.GRASS_BLOCK.getDefaultState();
				BlockState middleBlock=Blocks.STONE.getDefaultState();
				BlockState bottomBlock=Blocks.WHITE_STAINED_GLASS.getDefaultState();
				try {
					topBlock=((BiomeBase)this.getBiomeProvider().getNoiseBiome(x,0,z)).getTopBlock();
					middleBlock=((BiomeBase)this.getBiomeProvider().getNoiseBiome(x,0,z)).getMiddleBlock();
					bottomBlock=((BiomeBase)this.getBiomeProvider().getNoiseBiome(x,0,z)).getBottomBlock();
				} catch (Exception ignored) {}
				int ystart=generator.func_222529_a(x+chunkIn.getPos().getXStart(),z+chunkIn.getPos().getZStart(), Heightmap.Type.WORLD_SURFACE);
				boolean noblock=true;
				for (int i=ystart;i>=0;i--) {
					BlockPos pos=new BlockPos(x+chunkIn.getPos().getXStart(),i,z+chunkIn.getPos().getZStart());
					if (worldIn.getBlockState(pos).isAir(world,pos)) {
						if (!noblock) {
							if (worldIn.getBlockState(pos.up()).equals(middleBlock)) {
								worldIn.setBlockState(pos.up(),bottomBlock,0);
							}
						}
						noblock=true;
					} else {
						if (noblock) {
							worldIn.setBlockState(pos,topBlock,0);
						}
						noblock=false;
					}
				}
//				worldIn.setBlockState(new BlockPos(x+chunkIn.getPos().getXStart(),i-1,z+chunkIn.getPos().getZStart()),Blocks.GRASS_BLOCK.getDefaultState(),0);
//				while (!worldIn.getWorld().getBlockState(new BlockPos(x+chunkIn.getPos().getXStart(),i,z+chunkIn.getPos().getZStart())).isAir()) {
//					t++;
//					if (i==ystart) {
//						worldIn.setBlockState(new BlockPos(x+chunkIn.getPos().getXStart(),i,z+chunkIn.getPos().getZStart()),Blocks.GRASS.getDefaultState(),0);
//					} else {
//						if (worldIn.getBlockState(new BlockPos(x+chunkIn.getPos().getXStart(),i,z+chunkIn.getPos().getZStart()).down()).isAir()) {
//							worldIn.setBlockState(new BlockPos(x+chunkIn.getPos().getXStart(),i,z+chunkIn.getPos().getZStart()),Blocks.WHITE_STAINED_GLASS.getDefaultState(),0);
//						}
//					}
//					i--;
//					if (t>=60) {
//						break;
//					}
//				}
				
//				EndChunkGenerator generator=new EndChunkGenerator(worldIn,this.biomeProvider,new EndGenerationSettings());
//				int y=generator.func_222529_a((int)((x+chunkIn.getPos().asBlockPos().getX())*2),(int)((z+chunkIn.getPos().asBlockPos().getZ())*2), Heightmap.Type.WORLD_SURFACE);
//				int y2=(-(y-52))+0;
//				if (y!=0) {
//					y/=2;
//					y2/=3;
//					y2+=18;
//					if (y2<y) {
//						for (int i=y2+1;i<y;i++) {
//							worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,i+31,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.STONE.getDefaultState(),0);
//						}
//						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,y+31,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.GRASS_BLOCK.getDefaultState(),0);
//						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,y2+31,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.WHITE_STAINED_GLASS.getDefaultState(),0);
//					}
//				}
			}
		}
		
		//		for (int x=0;x<16;x++) {
		//			for (int z=0;z<16;z++) {
		////				int y=new EndChunkGenerator(worldIn,this.biomeProvider,new EndGenerationSettings()).func_222529_a((int)((x+chunkIn.getPos().asBlockPos().getX())*2),(int)((z+chunkIn.getPos().asBlockPos().getZ())*2), Heightmap.Type.WORLD_SURFACE);
		////				worldIn.setBlockState(chunkIn.getPos().asBlockPos().add(x,y,z),Blocks.LIME_WOOL.getDefaultState(),0);
		//	//				int ymin=(int)(
		//	//						Math.toDegrees(
		//	//								Math.tan(
		//	//										x+chunkIn.getPos().asBlockPos().getX()
		//	//								)) *Math.sin(
		//	//										z*x*chunkIn.getPos().asLong()
		//	//						)*Math.cos(
		//	//								Math.tan(x*z*2*2*chunkIn.getPos().getXStart()*worldIn.getSeed())
		//	//						)
		//	//				);
		//	//				int ymax=(int)(
		//	//						Math.toDegrees(
		//	//								Math.tan(
		//	//										x+chunkIn.getPos().asBlockPos().getX()
		//	//								)) *Math.cos(
		//	//								z*x*chunkIn.getPos().asLong()
		//	//						)*Math.sin(
		//	//								Math.tan(x*z*2*2*chunkIn.getPos().getXStart()*worldIn.getSeed())
		//	//						)
		//	//				);
		//	//				ymin/=16;
		//	//				ymax/=16;
		//	//				ymin-=(int)(Math.cos(x*z*chunkIn.getPos().getXStart()*chunkIn.getPos().getZStart()*worldIn.getSeed()*chunkIn.getPos().asLong()*chunkIn.getPos().asBlockPos().toLong())*12);
		//	//				ymax+=(int)(Math.sin(x*z*chunkIn.getPos().getXStart()*chunkIn.getPos().getZStart()*worldIn.getSeed()*chunkIn.getPos().asLong()*chunkIn.getPos().asBlockPos().toLong())*12);
		//	//				ymin*=0.5;
		//	//				ymax*=0.5;
		//				float xpos=x+chunkIn.getPos().getXStart();
		//				float zpos=z+chunkIn.getPos().getZStart();
		//				float divisor=12800f;
		//				xpos/=divisor;
		//				zpos/=divisor;
		//				int ymin=(int)(Math.cos(((5*(((((int)(xpos*divisor)^4)/divisor)*zpos*zpos)*divisor/128)))+Math.sin(xpos)+Math.cos(zpos))*7);
		//				ymin=(int)(Math.cos(ymin*xpos*xpos/divisor)*5);
		////				int ymin=(int)(Math.cos(((Math.sin(xpos)*xpos*zpos*3+Math.cos(zpos)*worldIn.getSeed()*3)*(Math.cos(Math.cos(Math.sin(Math.sin(((x/divisor)+1)*xpos*((z/divisor)+1)*zpos*worldIn.getSeed()))))*5))/divisor)*8);
		////				ymin=(int)(Math.cos((ymin+(xpos*zpos))*worldIn.getSeed())*7);
		//				int ymax=(int)(Math.sin((ymin*((xpos*zpos*zpos)*divisor/128)))*7);
		////				ymin=-128;
		////				System.out.println(ymin);
		////				System.out.println(ymax);
		////				ymax/=357913813;
		////				ymin/=357913813;
		////				ymax*=4;
		////				ymin*=4;
		//				try {
		//					if (ymin<ymax) {
		//						for (int i=ymin;i<=ymax;i++) {
		//							worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,chunkIn.getPos().asBlockPos().getY()+i+128,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.ORANGE_CONCRETE.getDefaultState(),0);
		//						}
		//						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,chunkIn.getPos().asBlockPos().getY()+ymin+128,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.LIME_WOOL.getDefaultState(),0);
		//						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,chunkIn.getPos().asBlockPos().getY()+ymax+128,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.GREEN_WOOL.getDefaultState(),0);
		//					} else {
		////						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,chunkIn.getPos().asBlockPos().getY()+ymin+128,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.COARSE_DIRT.getDefaultState(),0);
		////						worldIn.setBlockState(new BlockPos(chunkIn.getPos().asBlockPos().getX()+x,chunkIn.getPos().asBlockPos().getY()+ymax+128,chunkIn.getPos().asBlockPos().getZ()+z),Blocks.STONE.getDefaultState(),0);
		//					}
		//				} catch (Exception err) {}
		//			}
		//		}
	}
	
	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, @Nonnull Heightmap.Type heightmapType) {
		return 0;
	}
}
