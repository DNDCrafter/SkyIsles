package structure.test;

import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import com.thelastflames.skyisles.chunk_generators.si.Point;
import com.thelastflames.skyisles.chunk_generators.structure.SIStructureStart;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import structure.PlacementRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRules extends PlacementRules {
    SINoiseSettings densityNoise = new SINoiseSettings(
            0, "perlin_simplex",
            Arrays.asList(1, -1, -1),
            0.01, 0.01, 0.01, 1
    );
    SINoiseSettings densityNegationNoise = new SINoiseSettings(
            0, "perlin_simplex",
            Arrays.asList(1, -1, -1),
            0.01, 0.01, 0.01, 1
    );

    List<StructureType> types = new ArrayList<>();
    double maxSize;
    StructureType largestStructure;

    public TestRules(StructureType[] types) {
        for (StructureType type : types) {
            this.types.add(type);
            double mySize = Math.ceil(type.maxSize / 16d);

            if (mySize > maxSize)
                largestStructure = type;

            maxSize = Math.max(maxSize, mySize);
        }
    }

    @Override
    public List<StructurePotential> place(ChunkGenerator generator, PositionalRandomFactory factory, ChunkPos minCoord, ChunkPos maxCoord, ChunkPos active) {
        ArrayList<StructurePotential> points = new ArrayList<>();

        int origX = (int) (((minCoord.x / ((int) maxSize))) * maxSize);
        int origZ = (int) (((minCoord.z / ((int) maxSize))) * maxSize);

        int dstX = (int) (((maxCoord.x / ((int) maxSize)) + 1) * maxSize);
        int dstZ = (int) (((maxCoord.z / ((int) maxSize)) + 1) * maxSize);

        for (int x = origX; x < dstX; x++) {
            for (int z = origZ; z < dstZ; z++) {
                RandomSource rng = factory.fromHashOf("region_" + x + "_" + z);
                points.add(
                        new StructurePotential(
                                x + rng.nextInt((int) maxSize) + rng.nextDouble(),
                                x + rng.nextInt((int) maxSize) + rng.nextDouble(),
                                types.get(
                                        rng.nextInt(types.size())
                                ), true
                        )
                );
            }
        }

        return points;
    }

    @Override
    public List<SIStructureStart> prepare(ChunkGenerator generator, PositionalRandomFactory factory, List<Point> points) {
        return null;
    }
}
