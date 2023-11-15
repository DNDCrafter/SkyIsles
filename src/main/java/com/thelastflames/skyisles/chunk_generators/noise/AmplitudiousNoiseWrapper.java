package com.thelastflames.skyisles.chunk_generators.noise;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AmplitudiousNoiseWrapper extends NoiseWrapper {
    private final NoiseWrapper[] noiseLevels;
    private final int firstOctave;
    private final DoubleList amplitudes;
    private final double lowestFreqValueFactor;
    private final double lowestFreqInputFactor;
    private final double maxValue;

    public AmplitudiousNoiseWrapper(RandomSource pRandom, List<Integer> amplitudes, Function<RandomSource, NoiseWrapper> wrapperSupplier) {
        double[] doubles = new double[amplitudes.size()];
        this.firstOctave = amplitudes.get(0);
        for (int i = 0; i < amplitudes.size(); i++) doubles[i] = amplitudes.get(i);
        this.amplitudes = DoubleList.of(doubles);
        this.noiseLevels = new NoiseWrapper[amplitudes.size()];

        PositionalRandomFactory positionalrandomfactory = pRandom.forkPositional();

        for (int k = 0; k < amplitudes.size(); ++k) {
            if (this.amplitudes.get(k) != 0.0D) {
                int l = this.firstOctave + k;
                this.noiseLevels[k] = wrapperSupplier.apply(positionalrandomfactory.fromHashOf("octave_" + l));
            }
        }

        int i = this.amplitudes.size();
        int j = -this.firstOctave;
        this.lowestFreqInputFactor = Math.pow(2.0D, (double) (-j));
        this.lowestFreqValueFactor = Math.pow(2.0D, (double) (i - 1)) / (Math.pow(2.0D, (double) i) - 1.0D);
        this.maxValue = this.edgeValue(2.0D);
    }

    private double edgeValue(double p_210650_) {
        double d0 = 0.0D;
        double d1 = this.lowestFreqValueFactor;

        for (int i = 0; i < this.noiseLevels.length; ++i) {
            NoiseWrapper noise = this.noiseLevels[i];
            if (noise != null) d0 += this.amplitudes.getDouble(i) * p_210650_ * d1;

            d1 /= 2.0D;
        }

        return d0;
    }

    public static double wrap(double pValue) {
        return pValue - (double) Mth.lfloor(pValue / 3.3554432E7D + 0.5D) * 3.3554432E7D;
    }

    public final double get(double x, double y) {
        double d0 = 0.0D;
        double d1 = this.lowestFreqInputFactor;
        double d2 = this.lowestFreqValueFactor;

        for (int i = 0; i < this.noiseLevels.length; ++i) {
            NoiseWrapper improvednoise = this.noiseLevels[i];
            if (improvednoise != null) {
                double d3 = improvednoise.get(wrap(x * d1), wrap(y * d1));
                d0 += this.amplitudes.getDouble(i) * d3 * d2;
            }

            d1 *= 2.0D;
            d2 /= 2.0D;
        }

        return d0;
    }

    public final double get(double x, double y, double z) {
        double d0 = 0.0D;
        double d1 = this.lowestFreqInputFactor;
        double d2 = this.lowestFreqValueFactor;

        for (int i = 0; i < this.noiseLevels.length; ++i) {
            NoiseWrapper improvednoise = this.noiseLevels[i];
            if (improvednoise != null) {
                double d3 = improvednoise.get(wrap(x * d1), wrap(y * d1 / 1000.0), wrap(z * d1));
                d0 += this.amplitudes.getDouble(i) * d3 * d2;
            }

            d1 *= 2.0D;
            d2 /= 2.0D;
        }

        return d0;
    }
}
