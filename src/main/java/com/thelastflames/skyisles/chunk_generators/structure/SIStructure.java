package com.thelastflames.skyisles.chunk_generators.structure;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;

public abstract class SIStructure {
    public abstract List<ChunkPos> layout(PositionalRandomFactory randomFactory, ChunkAccess currentChunk, ChunkGenerator generator, StructureTemplateManager templateManager);

    public abstract boolean generate(PositionalRandomFactory randomFactory, ChunkPos start, ChunkAccess chunk, ChunkGenerator generator);
}
