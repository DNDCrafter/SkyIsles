package com.thelastflames.skyisles.chunk_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.si.Cell;
import com.thelastflames.skyisles.chunk_generators.si.Cluster;
import com.thelastflames.skyisles.chunk_generators.si.Point;
import com.thelastflames.skyisles.chunk_generators.structure.island.SmallIsland;
import com.thelastflames.skyisles.chunk_generators.structure.test.TestStructure;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkyIslesChunkGenerator3 extends ChunkGenerator {
    public static final Codec<SkyIslesChunkGenerator3> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
                SkyIslesGeneratorSettings.DIRECT_CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)
        ).apply(builder, builder.stable(SkyIslesChunkGenerator3::new));
    });

    private final SkyIslesGeneratorSettings settings;

    public SkyIslesChunkGenerator3(BiomeSource biomeSource, SkyIslesGeneratorSettings settings) {
        super(biomeSource);
        this.settings = settings;
    }

    @Override
    protected Codec<SkyIslesChunkGenerator3> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int iBlock = 3;
                boolean surface = true;

                int my = pChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 1;
                my = Math.min(my, pChunk.getMaxBuildHeight() + 3);

                if (my == -64) continue;

                // grass&dirt
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

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> this.doFill(pBlender, pStructureManager, pRandom, pChunk)), Util.backgroundExecutor()).whenCompleteAsync((p_224309_, p_224310_) -> {
            for (LevelChunkSection levelchunksection1 : pChunk.getSections()) {
                levelchunksection1.release();
            }
        }, pExecutor);
    }

    private ChunkAccess doFill(Blender pBlender, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
//        pStructureManager.structureCheck.structureTemplateManager.resourceManager;
        SmallIsland island = new SmallIsland();
        for (ChunkPos chunkPos : island.layout(
                pRandom.oreRandom(),
                pChunk, this,
                null
        )) {
            island.generate(
                    pRandom.oreRandom(),
                    chunkPos, pChunk,
                    this
            );
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
        return pLevel.getMinBuildHeight() - 1000; // no terrain
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        BlockState[] states = new BlockState[pHeight.getMaxBuildHeight() - pHeight.getMinBuildHeight()];
        for (int i = 0; i < states.length; i++)
            states[i] = Blocks.AIR.defaultBlockState();
        return new NoiseColumn(pHeight.getMinBuildHeight(), states);
    }

    int px;
    int pz;

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {
        pInfo.add("Y Range: " + settings.minY() + " -> " + settings.maxY());
        pInfo.add("Scale: " + settings.horizontalScale() + ", " + settings.verticalScale());
        pInfo.add("Island Bias: " + settings.bias());

        ChunkPos cpos = new ChunkPos(new BlockPos(pPos.getX(), 0, pPos.getZ()));
        Cell cell = new Cell(cpos.x, cpos.z);
        cell.distrobute(pRandom.oreRandom());
        pInfo.add("Cell: " + ChatFormatting.GREEN + cell.x + ", " + cell.z);
        pInfo.add("Colossus: " + ChatFormatting.GOLD + cell.isColossus);
        pInfo.add("Clusters: " + ChatFormatting.AQUA + cell.clusters.size());
        if (!cell.clusters.isEmpty()) {
            Cluster cluster = cell.clusters.get(0);
            int wx = cell.modX(pPos.getX());
            int wz = cell.modZ(pPos.getZ());
            double dist = cluster.sumDist(
                    null,
                    new Point(
                            wx / 16d, wz / 16d
                    )
            );
            dist = 1 - dist;
            dist = Math.pow(dist, 200);
            dist = 1 - dist;
            if (dist > 0) {
                pInfo.add("Dist To: " + ChatFormatting.AQUA + dist);
            } else {
                pInfo.add("Dist To: " + ChatFormatting.DARK_AQUA + dist);
            }
            pInfo.add("W: " + ChatFormatting.DARK_PURPLE + (wx / 16d) + ", " + (wz / 16d));
            pInfo.add("C: " + ChatFormatting.DARK_PURPLE + cluster.centerX + ", " + cluster.centerY);
        }

        int h = Minecraft.getInstance().level.getHeight(Heightmap.Types.MOTION_BLOCKING, pPos.getX(), pPos.getZ());
//        if (Minecraft.getInstance().level.getBlockState(
//                new BlockPos(pPos.getX(), 0, pPos.getZ())
//        ))
        if (h > 0) {
            px = pPos.getX();
            pz = pPos.getZ();
        }

        pInfo.add("From: " + px + ", " + pz);
        pInfo.add("To: " + pPos.getX() + ", " + pPos.getZ());
        pInfo.add("Dist: " + Math.sqrt(pPos.distToCenterSqr(
                px + 0.5, pPos.getY() + 0.5, pz + 0.5
        )));
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel pLevel, ChunkAccess pChunk, StructureManager pStructureManager) {
        super.applyBiomeDecoration(pLevel, pChunk, pStructureManager);
        TestStructure structure = new TestStructure();
        RandomSource src = RandomSource.create(pLevel.getSeed());
        PositionalRandomFactory factory = new XoroshiroRandomSource.XoroshiroPositionalRandomFactory(src.nextLong(), src.nextLong());
        for (ChunkPos chunkPos : structure.layout(factory, pChunk, this, null)) {
            structure.generate(factory, chunkPos, pChunk, this);
        }
    }

    @Override
    public void createStructures(RegistryAccess pRegistryAccess, ChunkGeneratorStructureState pStructureState, StructureManager pStructureManager, ChunkAccess pChunk, StructureTemplateManager pStructureTemplateManager) {
        super.createStructures(pRegistryAccess, pStructureState, pStructureManager, pChunk, pStructureTemplateManager);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {

    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> pStructureSetLookup, RandomState pRandomState, long pSeed) {
        return super.createState(pStructureSetLookup, pRandomState, pSeed);
    }
}
