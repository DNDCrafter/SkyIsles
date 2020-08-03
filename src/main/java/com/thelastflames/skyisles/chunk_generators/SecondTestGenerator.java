package com.thelastflames.skyisles.chunk_generators;

import com.thelastflames.skyisles.biomes.BiomeBase;
import com.thelastflames.skyisles.island_structures.Structure;
import com.thelastflames.skyisles.utils.random.Random1;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SecondTestGenerator extends ChunkGenerator<GenerationSettings> {
	public SecondTestGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, GenerationSettings generationSettingsIn) {
		super(worldIn, biomeProviderIn, generationSettingsIn);
//		generatorBottomPerlin=new PerlinNoiseGenerator(new SharedSeedRandom(worldIn.getSeed()*2),3,16);
		generatorBottomPerlin = new SimplexNoiseGenerator(new SharedSeedRandom(worldIn.getSeed() * 2));
		generatorBottom = new OctavesNoiseGenerator(new SharedSeedRandom(worldIn.getSeed() * 2), 3, 16);
		generatorTop = new PerlinNoiseGenerator(new SharedSeedRandom(worldIn.getSeed()), 3, 32);
	}
	
	@Override
	public void generateSurface(@Nonnull WorldGenRegion p_225551_1_, @Nonnull IChunk p_225551_2_) {
	}
	
	@Override
	public int getGroundHeight() {
		return 128;
	}
	
	@Override
	public void generateStructures(@Nonnull BiomeManager p_227058_1_, IChunk chunkIn, @Nonnull ChunkGenerator<?> p_227058_3_, @Nonnull TemplateManager p_227058_4_) {
	}
	
	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int topBlockPos = getGenerationHeight(chunkIn.getPos().getXStart() + x, chunkIn.getPos().getZStart() + z);
				int bottomBlockPos = getGenerationDepth(chunkIn.getPos().getXStart() + x, chunkIn.getPos().getZStart() + z);
				if (topBlockPos > bottomBlockPos) {
					BlockState topBlock = Blocks.GRASS_BLOCK.getDefaultState();
					BlockState middleBlock = Blocks.STONE.getDefaultState();
					BlockState bottomBlock = Blocks.WHITE_STAINED_GLASS.getDefaultState();
					Biome b = (this.getBiomeProvider().getNoiseBiome(chunkIn.getPos().getXStart() + x, 0, chunkIn.getPos().getZStart() + z));
					if (b instanceof BiomeBase) {
						topBlock = ((BiomeBase) b).getTopBlock();
						middleBlock = ((BiomeBase) b).getMiddleBlock();
						bottomBlock = ((BiomeBase) b).getBottomBlock();
					}
					
					Random1.setSeed(chunkIn.getPos().asBlockPos().add(x, topBlockPos, z).toLong() * this.world.getSeed());
					if (Random1.getBoolean(25)) {
						if (b instanceof BiomeBase) {
							ArrayList<Structure> structures = ((BiomeBase) b).getStructures();
							if (!structures.isEmpty()) {
								if (structures.size() == 1) {
									structures.get(0).spawn(x, topBlockPos + 1, z, chunkIn);
								} else {
									structures.get(Random1.getIntWithLimit(structures.size() - 1)).spawn(x, topBlockPos, z, chunkIn);
								}
							}
						}
					}
					
					for (int i = topBlockPos; i > bottomBlockPos; i--) {
						worldIn.setBlockState(new BlockPos(chunkIn.getPos().getXStart() + x, i, chunkIn.getPos().getZStart() + z), middleBlock, 0);
					}
					
					worldIn.setBlockState(new BlockPos(chunkIn.getPos().getXStart() + x, bottomBlockPos, chunkIn.getPos().getZStart() + z), bottomBlock, 0);
					worldIn.setBlockState(new BlockPos(chunkIn.getPos().getXStart() + x, topBlockPos, chunkIn.getPos().getZStart() + z), topBlock, 0);
				}
			}
		}
	}
	
	private final PerlinNoiseGenerator generatorTop;
	
	public int getGenerationHeight(int x, int z) {
		double top = generatorBottomPerlin.getValue(x / 4280f, z / 4280f);
		top *= generatorTop.noiseAt(x / 128f, z / 128f, true);
		top += generatorTop.noiseAt(x / 256f, z / 256f, true);
//		double top = generatorTop.noiseAt(x/128f, z/128f, true);
		return (int) (top * 128) + getGroundHeight();
	}
	
	private final SimplexNoiseGenerator generatorBottomPerlin;
	private final OctavesNoiseGenerator generatorBottom;
	
	public int getGenerationDepth(int x, int z) {
		double bottom = generatorBottomPerlin.getValue(x / 4280f, z / 4280f);
		bottom *= generatorBottom.noiseAt(x / 128f, z / 128f, 0, 256);
		bottom += generatorBottom.noiseAt(x / 256f, z / 256f, 0, 256);
//		return (int) (bottom * 128)+getGroundHeight();
		return (int) (bottom * 128) + getGroundHeight();
	}
	
	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type heightmapType) {
		return 0;
	}
}
