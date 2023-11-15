package com.thelastflames.skyisles.chunk_generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import com.thelastflames.skyisles.mixins.generation.ChunkAccessAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import com.thelastflames.skyisles.codec.CustomCodecs;

import java.util.Optional;
import java.util.stream.IntStream;

public record SkyIslesGeneratorSettings(
        BlockState defaultBlock, int minY, int maxY,
        double verticalScale, double horizontalScale,
        double bias, SINoiseSettings terrain, SINoiseSettings shape,
        Optional<Long> seed
) {
    private static final SINoiseSettings TERRAIN = new SINoiseSettings(
            0, "perlin", IntStream.of(1, 1, 2, 1, 2, 1, 0, 2, 0)
    );
    private static final SINoiseSettings SHAPE = new SINoiseSettings(
            0, "perlin_simplex", IntStream.of(1, -1, -1)
    );

    public static final Codec<SkyIslesGeneratorSettings> DIRECT_CODEC = RecordCodecBuilder.create((p_64475_) -> {
        return p_64475_.group(
                BlockState.CODEC.fieldOf("default_block").orElse(Blocks.AIR.defaultBlockState()).forGetter(SkyIslesGeneratorSettings::defaultBlock),
                Codec.INT.fieldOf("minY").orElse(0).forGetter(SkyIslesGeneratorSettings::minY),
                Codec.INT.fieldOf("maxY").orElse(128).forGetter(SkyIslesGeneratorSettings::maxY),
                Codec.DOUBLE.fieldOf("vertical_scale").orElse(64.0).forGetter(SkyIslesGeneratorSettings::verticalScale),
                Codec.DOUBLE.fieldOf("horizontal_scale").orElse(1000.0).forGetter(SkyIslesGeneratorSettings::horizontalScale),
                Codec.DOUBLE.fieldOf("island_bias").orElse(0.5).forGetter(SkyIslesGeneratorSettings::bias),
                SINoiseSettings.CODEC.fieldOf("terrain_noise").orElse(TERRAIN).forGetter(SkyIslesGeneratorSettings::terrain),
                SINoiseSettings.CODEC.fieldOf("shape_noise").orElse(SHAPE).forGetter(SkyIslesGeneratorSettings::shape),
                CustomCodecs.OPTIONAL_LONG.fieldOf("seed").forGetter(SkyIslesGeneratorSettings::seed)
        ).apply(p_64475_, SkyIslesGeneratorSettings::new);
    });

    public WorldgenRandom.Algorithm getRandomSource() {
        return WorldgenRandom.Algorithm.XOROSHIRO;
    }

    public long getSeed(ChunkAccess pChunk) {
        if (seed.isPresent())
            return seed.get();

        LevelHeightAccessor accessor = ((ChunkAccessAccessor) pChunk).getLevelHeightAccessor();
        if (accessor instanceof ServerLevel) return ((ServerLevel) accessor).getSeed();
        return 0;
    }
}

