package com.thelastflames.skyisles.chunk_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import com.thelastflames.skyisles.chunk_generators.surface.SkyIslandSurfaceConfig;
import com.thelastflames.skyisles.mixins.generation.ChunkAccessAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import com.thelastflames.skyisles.codec.CustomCodecs;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public record SkyIslesGeneratorSettings(
        int minY, int maxY,
        double verticalScale, double horizontalScale, double bias, // terrain shapping
        SINoiseSettings terrain, SINoiseSettings shape, SINoiseSettings bottom, // noise
        SkyIslandSurfaceConfig surfaceConfig, // yeh
        Optional<Long> seed // mojang, why don't you provide a seed o world generators when they're created?
) {
    public static final SkyIslandSurfaceConfig DEFAULT_SURFACE = new SkyIslandSurfaceConfig(
            Blocks.STONE.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Optional.of(Blocks.GRAVEL.defaultBlockState()),
            Arrays.asList(Blocks.STONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState())
    );

    private static final SINoiseSettings TERRAIN = new SINoiseSettings(
            0, "simplex_amplitudes", IntStream.of(1, 1, 2, 1, 2, 1, 0, 2, 0),
            0.5, 0.001, 0.5, 1
    );
    private static final SINoiseSettings SHAPE = new SINoiseSettings(
            0, "perlin_simplex", IntStream.of(1, -1, -1),
            1, 1, 1, 1
    );
    private static final SINoiseSettings BOTTOM = new SINoiseSettings(
            0, "simplex_amplitudes", IntStream.of(1, 1, 2, 1, 2, 3, 2, 1, 0),
            1, 1, 1, 0.2
    );

    public static final Codec<SkyIslesGeneratorSettings> DIRECT_CODEC = RecordCodecBuilder.create((p_64475_) -> {
        return p_64475_.group(
                Codec.INT.fieldOf("minY").orElse(0).forGetter(SkyIslesGeneratorSettings::minY),
                Codec.INT.fieldOf("maxY").orElse(128).forGetter(SkyIslesGeneratorSettings::maxY),
                Codec.DOUBLE.fieldOf("vertical_scale").orElse(64.0).forGetter(SkyIslesGeneratorSettings::verticalScale),
                Codec.DOUBLE.fieldOf("horizontal_scale").orElse(1000.0).forGetter(SkyIslesGeneratorSettings::horizontalScale),
                Codec.DOUBLE.fieldOf("island_bias").orElse(0.5).forGetter(SkyIslesGeneratorSettings::bias),
                SINoiseSettings.CODEC.fieldOf("terrain_noise").orElse(TERRAIN).forGetter(SkyIslesGeneratorSettings::terrain),
                SINoiseSettings.CODEC.fieldOf("shape_noise").orElse(SHAPE).forGetter(SkyIslesGeneratorSettings::shape),
                SINoiseSettings.CODEC.fieldOf("bottom_noise").orElse(BOTTOM).forGetter(SkyIslesGeneratorSettings::bottom),
                SkyIslandSurfaceConfig.CODEC.fieldOf("surface").orElse(DEFAULT_SURFACE).forGetter(SkyIslesGeneratorSettings::surfaceConfig),
                CustomCodecs.OPTIONAL_LONG.fieldOf("seed").forGetter(SkyIslesGeneratorSettings::seed)
        ).apply(p_64475_, SkyIslesGeneratorSettings::new);
    });

    public WorldgenRandom.Algorithm getRandomSource() {
        return WorldgenRandom.Algorithm.XOROSHIRO;
    }

    public long getSeed(LevelHeightAccessor pLevel) {
        if (seed.isPresent())
            return seed.get();

        if (pLevel instanceof ChunkAccess) return getSeed((ChunkAccess) pLevel);
        if (pLevel instanceof ServerLevel) return ((ServerLevel) pLevel).getSeed();
        return 0;
    }

    public long getSeed(ChunkAccess pChunk) {
        if (seed.isPresent())
            return seed.get();

        LevelHeightAccessor accessor = ((ChunkAccessAccessor) pChunk).getLevelHeightAccessor();
        if (accessor instanceof ServerLevel) return ((ServerLevel) accessor).getSeed();
        return 0;
    }
}

