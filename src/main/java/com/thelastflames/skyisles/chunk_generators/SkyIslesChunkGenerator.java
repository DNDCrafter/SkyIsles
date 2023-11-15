package com.thelastflames.skyisles.chunk_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.noise.NoiseWrapper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkyIslesChunkGenerator extends ChunkGenerator {
    public static final Codec<SkyIslesChunkGenerator> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
                SkyIslesGeneratorSettings.DIRECT_CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)
        ).apply(builder, builder.stable(SkyIslesChunkGenerator::new));
    });

    private final SkyIslesGeneratorSettings settings;

    //    PerlinNoise noise = PerlinNoise.create(
//            settings.getRandomSource().newInstance(432532),
//            0, 1, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 2.0, 0.0
//    );
//    PerlinSimplexNoise noiseShape = new PerlinSimplexNoise(
//            settings.getRandomSource().newInstance(432532),
//            Arrays.asList(1, -1, -1)
//    );

    public SkyIslesChunkGenerator(BiomeSource biomeSource, SkyIslesGeneratorSettings settings) {
        super(biomeSource);
        this.settings = settings;
    }

    @Override
    protected Codec<SkyIslesChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
        int i = pChunk.getMinBuildHeight();
        int j = Mth.floorDiv(i, 16);
        int k = Mth.floorDiv(pChunk.getMaxBuildHeight(), 16);
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> {
            return this.doFill(pBlender, pStructureManager, pRandom, pChunk, j, k);
        }), Util.backgroundExecutor()).whenCompleteAsync((p_224309_, p_224310_) -> {
            for (LevelChunkSection levelchunksection1 : pChunk.getSections()) {
                levelchunksection1.release();
            }
        }, pExecutor);
    }

    private ChunkAccess doFill(Blender pBlender, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk, int pMinCellY, int pCellCountY) {
        NoiseWrapper noise;
        NoiseWrapper noiseShape;
        long seed = settings.getSeed(pChunk);
        noise = settings.terrain().create(seed);
        noiseShape = settings.shape().create(seed);

        int mx = pChunk.getPos().getMinBlockX();
        int mz = pChunk.getPos().getMinBlockZ();

        double hscl = settings.horizontalScale();
        double bias = settings.bias();
        double inv_bias = 1 - bias;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double sv = noiseShape.get((x + mx) / hscl, (z + mz) / hscl) + 0.5;

                if (sv > bias) {
                    for (int y = pChunk.getMinBuildHeight(); y < pChunk.getMaxBuildHeight(); y++) {
                        if (y < settings.minY()) continue;
                        if (y > settings.maxY()) continue;

                        double v = noise.get((x + mx) / hscl, y / 100.0, (z + mz) / hscl) + 0.5;

                        double scl = y - (settings.maxY() - settings.minY()) / 2d;
                        scl /= settings.verticalScale() / 2d;
                        scl = Math.abs(scl);
                        if (scl < 0) scl = 0;
                        v += 1 - scl;
                        v *= (sv - bias) * (1 / inv_bias);

                        if (v > 0.5) {
                            if (x == 0 || z == 0) {
                                pChunk.setBlockState(
                                        new BlockPos(x, y, z),
                                        Blocks.GOLD_BLOCK.defaultBlockState(),
                                        false
                                );
                            } else if (x == 15 || z == 15) {
                                pChunk.setBlockState(
                                        new BlockPos(x, y, z),
                                        Blocks.DIAMOND_BLOCK.defaultBlockState(),
                                        false
                                );
                            } else {
                                pChunk.setBlockState(
                                        new BlockPos(x, y, z),
                                        Blocks.STONE.defaultBlockState(),
                                        false
                                );
                            }
                        }
                    }
                }
            }
        }

        return pChunk;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return this.settings.minY();
    }

    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState
            pRandom) {
        return (this.settings.minY() + this.settings.maxY()) / 2;
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        BlockState[] states = new BlockState[pHeight.getMaxBuildHeight() - pHeight.getMinBuildHeight()];
        for (int i = 0; i < states.length; i++) {
            states[i] = Blocks.AIR.defaultBlockState();
        }
        return new NoiseColumn(pHeight.getMinBuildHeight(), states);
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {

    }
}
