package com.thelastflames.skyisles.chunk_generators.noise;

public abstract class NoiseWrapper {
    public abstract double get(double x, double y);
    public abstract double get(double x, double y, double z);
}
