package com.thelastflames.skyisles.chunk_generators.structure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

import java.util.function.Supplier;

public class SIStructureStart {
    ChunkPos pos;
    SIStructure structure;
    Supplier<RandomSource> rng;
}
