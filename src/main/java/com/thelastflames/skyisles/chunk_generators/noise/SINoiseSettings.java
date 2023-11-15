package com.thelastflames.skyisles.chunk_generators.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record SINoiseSettings(
        long seedOffset, String type, List<Integer> amplitudes,
        double xFactor, double yFactor, double zFactor,
        double scale
) {
    public static final Codec<SINoiseSettings> CODEC = RecordCodecBuilder.create((p_64475_) -> {
        return p_64475_.group(
                Codec.LONG.fieldOf("seed_offset").orElse(0L).forGetter(SINoiseSettings::seedOffset),
                Codec.STRING.fieldOf("type").orElse("perlin").forGetter(SINoiseSettings::type),
                Codec.INT_STREAM.fieldOf("amplitudes").forGetter(SINoiseSettings::gamplitudes),
                Codec.DOUBLE.fieldOf("x_factor").orElse(1.0).forGetter(SINoiseSettings::xFactor),
                Codec.DOUBLE.fieldOf("y_factor").orElse(1.0).forGetter(SINoiseSettings::yFactor),
                Codec.DOUBLE.fieldOf("z_factor").orElse(1.0).forGetter(SINoiseSettings::zFactor),
                Codec.DOUBLE.fieldOf("scale").orElse(1.0).forGetter(SINoiseSettings::scale)
        ).apply(p_64475_, SINoiseSettings::new);
    });

    public SINoiseSettings(long seedOffset, String type, IntStream amplitudes, double xFactor, double yFactor, double zFactor, double scale) {
        this(seedOffset, type, toList(amplitudes), xFactor, yFactor, zFactor, scale);
    }

    private static ArrayList<Integer> toList(IntStream stream) {
        ArrayList<Integer> ints = new ArrayList<>();
        stream.forEach(ints::add);
        return ints;
    }

    public IntStream gamplitudes() {
        IntStream.Builder builder = IntStream.builder();
        for (Integer amplitude : amplitudes)
            builder.add(amplitude);
        return builder.build();
    }

    public NoiseWrapper create(long seed) {
        RandomSource rng = RandomSource.create(seed + seedOffset);
        switch (type) {
            case "improved":
                return new NoiseWrapper() {
                    final ImprovedNoise nz = new ImprovedNoise(rng);

                    public double get(double x, double y) {
                        return nz.noise(x * xFactor, y * zFactor, 0) * scale;
                    }

                    public double get(double x, double y, double z) {
                        return nz.noise(x * xFactor, y * yFactor, z * zFactor) * scale;
                    }
                };
            case "simplex_amplitudes":
                return new AmplitudiousNoiseWrapper(rng, amplitudes, (src) -> {
                    return new NoiseWrapper() {
                        final SimplexNoise nz = new SimplexNoise(src);

                        @Override
                        public double get(double x, double y) {
                            return nz.getValue(x * xFactor, y * zFactor) * scale;
                        }

                        @Override
                        public double get(double x, double y, double z) {
                            return nz.getValue(x * xFactor, y * yFactor, z * zFactor) * scale;
                        }
                    };
                });
            case "simplex":
                return new NoiseWrapper() {
                    final SimplexNoise nz = new SimplexNoise(rng);

                    @Override
                    public double get(double x, double y) {
                        return nz.getValue(x * xFactor, y * zFactor) * scale;
                    }

                    @Override
                    public double get(double x, double y, double z) {
                        return nz.getValue(x * xFactor, y * yFactor, z * zFactor) * scale;
                    }
                };
            case "perlin":
                return new NoiseWrapper() {
                    final PerlinNoise nz = PerlinNoise.create(
                            rng,
                            amplitudes
                    );

                    @Override
                    public double get(double x, double y) {
                        return nz.getValue(x * xFactor, y * zFactor, 0) * scale;
                    }

                    @Override
                    public double get(double x, double y, double z) {
                        return nz.getValue(x * xFactor, y * yFactor, z * zFactor) * scale;
                    }
                };
            case "perlin_simplex":
                return new NoiseWrapper() {
                    final PerlinSimplexNoise nz = new PerlinSimplexNoise(
                            rng, amplitudes
                    );

                    @Override
                    public double get(double x, double y) {
                        return nz.getValue(x * xFactor, y * zFactor, true) * scale;
                    }

                    @Override
                    public double get(double x, double y, double z) {
                        // TODO: emulate 3D noise?
//                        return nz.getValue(x, y, true);
                        throw new RuntimeException("perlin_simplex cannot be used for 3d");
                    }
                };
        }
        throw new RuntimeException("Non existent noise implementation: " + type);
    }
}
