package com.thelastflames.skyisles.chunk_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.noise.NoiseWrapper;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

public class SkyIslesChunkGenerator extends ChunkGenerator {
    public static final Codec<SkyIslesChunkGenerator> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
                SkyIslesGeneratorSettings.DIRECT_CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)
        ).apply(builder, builder.stable(SkyIslesChunkGenerator::new));
    });

    private final SkyIslesGeneratorSettings settings;

    public SkyIslesChunkGenerator(BiomeSource biomeSource, SkyIslesGeneratorSettings settings) {
        super(biomeSource);
        this.settings = settings;
    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> pStructureSetLookup, RandomState pRandomState, long pSeed) {
        return super.createState(pStructureSetLookup, pRandomState, pSeed);
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
        int midY = (settings.maxY() - settings.minY()) / 2;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int iBlock = 3;
                boolean surface = true;
                int my = pChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 1;
                my = Math.min(my, pChunk.getMaxBuildHeight() + 3);
                boolean iEdge = (my - 1) == midY;

                if (iEdge) {
                    Optional<BlockState> overhang = settings.surfaceConfig().overhangState();
                    boolean iEdgeA = false;
                    if (overhang.isPresent())
                        iEdgeA = pChunk.getBlockState(new BlockPos(x, my - 1, z)).equals(Blocks.OBSIDIAN.defaultBlockState());
                    // stone
                    if (iEdgeA) {
                        pChunk.setBlockState(new BlockPos(x, my - 1, z), overhang.get(), false);
                    } else {
                        BlockPos cPos = new BlockPos(x, my - 1, z);

                        int rand = pRandom.oreRandom().at(cPos).nextInt(settings.surfaceConfig().edgeStates().size());
                        pChunk.setBlockState(new BlockPos(x, my - 1, z), settings.surfaceConfig().edgeStates().get(rand), false);
                    }
                } else {
                    // grass
                    for (int y = my; y >= pChunk.getMinBuildHeight(); y--) {
                        BlockState state = pChunk.getBlockState(new BlockPos(x, y, z));

                        boolean iiBlock = state.isAir();
                        if (iBlock > 0) {
                            if (!iiBlock) {
                                pChunk.setBlockState(new BlockPos(x, y, z),
                                        surface ?
                                                settings.surfaceConfig().surfaceState() :
                                                settings.surfaceConfig().dirtState()
                                        , false);
                            }
                        } else if (!iiBlock) {
                            pChunk.setBlockState(new BlockPos(x, y, z), settings.surfaceConfig().baseState(), false);
                        }

                        if (my <= midY) {
                            iBlock = 0;
                        } else {
                            if (!iiBlock) {
                                iBlock -= 1;
                                surface = false;
                            } else {
                                iBlock = pRandom.oreRandom().at(x, y, z).nextInt(2, 4);
                                surface = true;
                            }
                        }
                    }
                }
            }
        }
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
        long seed = settings.getSeed(pChunk);
        NoiseWrapper noise = settings.terrain().create(seed);
        NoiseWrapper noiseShape = settings.shape().create(seed);
        NoiseWrapper noiseBottom = settings.bottom().create(seed);

        int mx = pChunk.getPos().getMinBlockX();
        int mz = pChunk.getPos().getMinBlockZ();

        double hscl = settings.horizontalScale();
        double bias = settings.bias();
        double inv_bias = 1 - bias;

        int middleY = (settings.maxY() - settings.minY()) / 2;

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

                        boolean would = (v * (sv - bias) * (1 / inv_bias)) > 0.5;
                        // rough bottom
                        if (y <= middleY) {
                            v -= noiseBottom.get((mx + x) / 30.0, (mz + z) / 30.0);
                        }

                        v *= (sv - bias) * (1 / inv_bias);

                        if (v > 0.5) {
                            if (!would && y >= middleY) {
                                pChunk.setBlockState(
                                        new BlockPos(x, y, z),
                                        Blocks.OBSIDIAN.defaultBlockState(),
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
        long seed = settings.getSeed(pLevel);
        NoiseWrapper noise = settings.terrain().create(seed);
        NoiseWrapper noiseShape = settings.shape().create(seed);
        NoiseWrapper noiseBottom = settings.bottom().create(seed);

        if (pLevel instanceof ProtoChunk) {
            if (((ProtoChunk) pLevel).getStatus() != ChunkStatus.EMPTY) {
                ChunkPos cp = new ChunkPos(new BlockPos(pX, 0, pZ));
                return ((ProtoChunk) pLevel).getHeight(pType, pX - cp.getMinBlockX(), pZ - cp.getMinBlockZ());
            }
        }

        int midY = (this.settings.minY() + this.settings.maxY()) / 2;
        double hscl = this.settings.horizontalScale();
        double bias = settings.bias();
        double inv_bias = 1 - bias;

        double sv = noiseShape.get((pX) / hscl, (pZ) / hscl) + 0.5;
        for (int y = midY; y < pLevel.getMaxBuildHeight(); y++) {
            if (y < settings.minY()) continue;
            if (y > settings.maxY()) continue;

            double v = noise.get((pX) / hscl, y / 100.0, (pZ) / hscl) + 0.5;

            double scl = y - (settings.maxY() - settings.minY()) / 2d;
            scl /= settings.verticalScale() / 2d;
            scl = Math.abs(scl);
            if (scl < 0) scl = 0;
            v += 1 - scl;

            // rough bottom
            if (y <= midY) {
                v -= noiseBottom.get((pX) / 30.0, (pZ) / 30.0, 0) * 0.2;
            }

            v *= (sv - bias) * (1 / inv_bias);

            if (v < 0.5) {
                if (y == midY)
                    return pLevel.getMinBuildHeight() - 1000; // no terrain

                return y;
            }
        }

        return pLevel.getMinBuildHeight() - 1000; // no terrain
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        BlockState[] states = new BlockState[pHeight.getMaxBuildHeight() - pHeight.getMinBuildHeight()];
        for (int i = 0; i < states.length; i++)
            states[i] = Blocks.AIR.defaultBlockState();
        return new NoiseColumn(pHeight.getMinBuildHeight(), states);
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {
        pInfo.add("Y Range: " + settings.minY() + " -> " + settings.maxY());
        pInfo.add("Scale: " + settings.horizontalScale() + ", " + settings.verticalScale());
        pInfo.add("Island Bias: " + settings.bias());
    }
}
