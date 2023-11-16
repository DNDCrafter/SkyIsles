package com.thelastflames.skyisles.chunk_generators.structure.test;

import com.thelastflames.skyisles.chunk_generators.structure.SIStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.List;

public class TestStructure extends SIStructure {
    @Override
    public List<ChunkPos> layout(PositionalRandomFactory randomFactory, ChunkAccess currentChunk, ChunkGenerator generator, StructureTemplateManager templateManager) {
        List<ChunkPos> poses = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                RandomSource source = randomFactory.fromHashOf("structure_" + "test" + "_" + (currentChunk.getPos().x + x) + ", " + (currentChunk.getPos().z + z));
                if (source.nextDouble() > 0.995) {
                    poses.add(
                            new ChunkPos(
                                    currentChunk.getPos().x + x,
                                    currentChunk.getPos().z + z
                            )
                    );
                }
            }
        }

        return poses;
    }

    protected boolean inBounds(int x, int z, ChunkAccess access) {
        if (access.getPos().getMinBlockZ() <= z && access.getPos().getMaxBlockZ() >= z) {
            if (access.getPos().getMinBlockX() <= x && access.getPos().getMaxBlockX() >= x) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean generate(PositionalRandomFactory randomFactory, ChunkPos start, ChunkAccess chunk, ChunkGenerator generator) {
        RandomSource source = randomFactory.fromHashOf("structure_gen_" + "test" + "_" + (start.x) + ", " + (start.z));
        for (int i = 0; i < 5; i++) {
            int xo = source.nextInt(15);
            int zo = source.nextInt(15);
            int yc = generator.getBaseHeight(start.getMinBlockX() + xo, start.getMinBlockZ() + zo, Heightmap.Types.WORLD_SURFACE_WG, chunk, null);
            if (yc < 0) continue;

            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (inBounds(
                            x + xo + start.getMinBlockX(),
                            z + zo + start.getMinBlockZ(),
                            chunk
                    )) {
                        yc = generator.getBaseHeight(start.getMinBlockX() + xo + x, start.getMinBlockZ() + zo + z, Heightmap.Types.WORLD_SURFACE_WG, chunk, null);
                        if (yc < 0) continue;

                        chunk.setBlockState(
                                new BlockPos(
                                        x + xo + start.getMinBlockX(),
                                        yc + 1,
                                        z + zo + start.getMinBlockZ()
                                ),
                                Blocks.OBSIDIAN.defaultBlockState(),
                                false
                        );
                        chunk.setBlockState(
                                new BlockPos(
                                        x + xo + start.getMinBlockX(),
                                        yc + 2,
                                        z + zo + start.getMinBlockZ()
                                ),
                                Blocks.OBSIDIAN.defaultBlockState(),
                                false
                        );
                        chunk.setBlockState(
                                new BlockPos(
                                        x + xo + start.getMinBlockX(),
                                        yc + 3,
                                        z + zo + start.getMinBlockZ()
                                ),
                                Blocks.OBSIDIAN.defaultBlockState(),
                                false
                        );
                    }
                }
            }

            return true;
        }

        return false;
    }
}
