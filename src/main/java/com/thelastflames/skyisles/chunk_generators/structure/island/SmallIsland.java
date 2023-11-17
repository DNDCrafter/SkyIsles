package com.thelastflames.skyisles.chunk_generators.structure.island;

import com.thelastflames.skyisles.chunk_generators.noise.NoiseWrapper;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import com.thelastflames.skyisles.chunk_generators.structure.SIStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmallIsland extends SIStructure {
    @Override
    public List<ChunkPos> layout(PositionalRandomFactory randomFactory, ChunkAccess currentChunk, ChunkGenerator generator, StructureTemplateManager templateManager) {
        List<ChunkPos> poses = new ArrayList<>();
        poses.add(new ChunkPos(0, 0));
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

        int mx = chunk.getPos().getMinBlockX();
        int mz = chunk.getPos().getMinBlockZ();

        int cx = start.x + 8;
        int cz = start.z + 8;

        double wobbleStart = source.nextDouble() * 2 * Math.PI;
        double wobbleRange = source.nextDouble() * 5;
        double wobbleRate = source.nextDouble() * 10 + 1;
        double diameter = source.nextDouble() * 20 + 10;
        double orientation = source.nextDouble() * 2 * Math.PI;
        double oblonginess = source.nextDouble() / 2.0;
        wobbleRange += diameter / 10;
        wobbleRate += diameter / 10;

        SINoiseSettings settings = new SINoiseSettings(
                0, "simplex_amplitudes",
                (List<Integer>) Arrays.asList(1, 1, 2, 1, 2, 0, 1),
                0.01, 0.01, 0.01, 1
        );
        NoiseWrapper wrapper = settings.create(source.nextLong());

        double sqrtDiam = (diameter + wobbleRange);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double dist = Math.sqrt(
                        Math.pow((x + mx) - cx, 2) +
                                Math.pow((z + mz) - cz, 2)
                );

                if (dist > sqrtDiam) continue;

                double ang = Math.atan2(
                        (x + mx) - cx,
                        (z + mz) - cz
                ) + wobbleStart;
                ang = ang % (2 * Math.PI);

                double c = Math.cos(ang + orientation);
//                double s = Math.sin(ang);

//                double relX = (x + mx) - cx;
//                double relZ = (z + mz) - cz;
//                double rx = relX * c + relZ * s;
//                double rz = relX * s - relZ * c;

                double noise = (wrapper.get(ang * wobbleRate, 0) * wobbleRange) + diameter;

                if ((ang / (2 * Math.PI)) > 0.75) {
                    double pct = (ang / (2 * Math.PI)) - 0.75;
                    pct *= 1 / 0.25;
                    pct *= pct;

                    noise = Mth.lerp(
                            pct,
                            noise,
                            (wrapper.get(((pct / 4) * Math.PI * 2) * wobbleRate, 0) * wobbleRange) + diameter
                    );
                }

//                double aspect = (Math.abs(rx / rz));
//                if (aspect > 1) aspect = 1 / aspect;
//                noise *= 1 - (aspect * oblonginess);

                noise *= 1 - (((c / 2) + 1) * oblonginess);

                if (dist < noise) {
                    chunk.setBlockState(
                            new BlockPos(x, 0, z),
                            Blocks.STONE.defaultBlockState(),
                            false
                    );

                    double nv = wrapper.get(x + mx, z + mz) + 0.5;
                    if (nv < 0.25) nv = 0.25;
                    nv += 0.25;

                    double dv = Math.abs(noise - dist);
                    dv /= sqrtDiam;
                    dv = Math.sqrt(dv);

                    int hght = (int) (dv * nv * 15);

                    for (int i = 0; i < hght; i++) {
                        chunk.setBlockState(
                                new BlockPos(x, i, z),
                                Blocks.STONE.defaultBlockState(),
                                false
                        );
                    }
                }
            }
        }

        return false;
    }
}
