import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    int x, z;
    boolean isColossus = false;
    List<Cluster> clusters = new ArrayList<>();

    int celSize = 48;

    protected int modulate(int xv) {
        double d = xv / ((double) celSize);
        if (d < 0) {
            if (d != (int) d)
                d -= 1;
        }
        return (int) d;
    }

    public Cell(int chunkX, int chunkZ) {
        this.x = modulate(chunkX);
        this.z = modulate(chunkZ);
    }

    public static Cell of(int celX, int celZ) {
        Cell cell = new Cell(0, 0);
        cell.x = celX;
        cell.z = celZ;
        return cell;
    }

    public void distrobute(PositionalRandomFactory factory) {
        RandomSource source = factory.fromHashOf("cell_" + x + "_" + z);

//        isColossus = source.nextDouble() > 0.95;
        isColossus = (x == 0 && z == 0);
        if (isColossus) {
            // subcells help mask rng bias
            int subCelSize = 8;
            int subCels = celSize / subCelSize;

            Cluster current = new Cluster(source.nextDouble() + 0.5, (source.nextDouble() + 0.5) * 5);

            int celX = source.nextInt(0, subCels);
            int celY = source.nextInt(0, subCels);

            double centerX = source.nextDouble() + source.nextInt(subCelSize) + celX * subCelSize;
            double centerY = source.nextDouble() + source.nextInt(subCelSize) + celY * subCelSize;

            Point prev = new Point(centerX, centerY);

            for (int i1 = 0; i1 < source.nextInt(10, 40); i1++) {
                current.pts.add(new Point(
                        prev.x + source.nextDouble() * current.terrainRange,
                        prev.y + source.nextDouble() * current.terrainRange
                ));
//                prev = current.pts.get(source.nextInt(0, current.pts.size()));
            }

            current.centerX = centerX;
            current.centerY = centerY;
            current.colossus = true;

            clusters.add(current);
        }

        // subcells help mask rng bias
        int subCelSize = 8;
        int subCels = celSize / subCelSize;

        for (int i = 0; i < source.nextInt(1, isColossus ? 2 : 8); i++) {
            Cluster current = new Cluster((source.nextDouble() + 0.5) / 2.0, (source.nextDouble() + 0.5) * 1.5);

            int celX = source.nextInt(0, subCels);
            int celY = source.nextInt(0, subCels);

            double centerX = source.nextDouble() + source.nextInt(subCelSize) + celX * subCelSize;
            double centerY = source.nextDouble() + source.nextInt(subCelSize) + celY * subCelSize;

            Point prev = new Point(centerX, centerY);

            for (int i1 = 0; i1 < source.nextInt(4, 8); i1++) {
                current.pts.add(new Point(
                        prev.x + source.nextDouble() * current.terrainRange,
                        prev.y + source.nextDouble() * current.terrainRange
                ));
            }

//            for (int i1 = 1; i1 < current.pts.size(); i1++) {
//                Point currentPt = current.pts.get(i1);
//
//                    Point closest = null;
//                    double closestDist = Double.POSITIVE_INFINITY;
//                    for (Point point : current.pts) {
//                        double d = point.distance(currentPt);
//                        if (d != 0 && d < closestDist) {
//                            closestDist = d;
//                            closest = point;
//                        }
//                    }
//
//                    if (closest != null) {
//                        if (closestDist < current.terrainRange)
//                            continue;
//
//                        currentPt.setDistance(closest, source.nextDouble() * current.terrainRange);
//                    }
//            }

            clusters.add(current);
        }

        for (int i = clusters.size() - 1; i >= 0; i--) {
            boolean rem = false;
            Cluster current = clusters.get(i);
            for (Cluster cluster : clusters) {
                if (!cluster.equals(current)) {
                    if (cluster.distTo(current) < (Math.max(cluster.obstructRange, current.obstructRange))) {
                        rem = true;
                        break;
                    }
                }
            }
            if (rem) {
                clusters.remove(i);
            }
        }
    }

    public void trim(
            Cell cell20, Cell cell21, Cell cell22,
            Cell cell10, /*        */ Cell cell12,
            Cell cell00, Cell cell01, Cell cell02
    ) {
        if (isColossus) {
            return;
        }

        ArrayList<Cluster> toRemove = new ArrayList<>();
        for (Cluster cluster : cell00.clusters) {
            for (Cluster cluster1 : clusters) {
                if (cluster.distTo(cluster1, -48, -48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                    toRemove.add(cluster1);
                }
            }
        }
        for (Cluster cluster : cell01.clusters) {
            for (Cluster cluster1 : clusters) {
                if (cluster.distTo(cluster1, -48, 0) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                    toRemove.add(cluster1);
                }
            }
        }
        for (Cluster cluster : cell10.clusters) {
            for (Cluster cluster1 : clusters) {
                if (cluster.distTo(cluster1, 0, -48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                    toRemove.add(cluster1);
                }
            }
        }

        if (cell12.isColossus) {
            for (Cluster cluster : cell12.clusters) {
                for (Cluster cluster1 : clusters) {
                    if (cluster.distTo(cluster1, 0, 48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                        toRemove.add(cluster1);
                    }
                }
            }
        }
        if (cell22.isColossus) {
            for (Cluster cluster : cell22.clusters) {
                for (Cluster cluster1 : clusters) {
                    if (cluster.distTo(cluster1, 48, 48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                        toRemove.add(cluster1);
                    }
                }
            }
        }
        if (cell21.isColossus) {
            for (Cluster cluster : cell21.clusters) {
                for (Cluster cluster1 : clusters) {
                    if (cluster.distTo(cluster1, 48, 0) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                        toRemove.add(cluster1);
                    }
                }
            }
        }
        if (cell20.isColossus) {
            for (Cluster cluster : cell20.clusters) {
                for (Cluster cluster1 : clusters) {
                    if (cluster.distTo(cluster1, -48, 48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                        toRemove.add(cluster1);
                    }
                }
            }
        }
        if (cell02.isColossus) {
            for (Cluster cluster : cell02.clusters) {
                for (Cluster cluster1 : clusters) {
                    if (cluster.distTo(cluster1, 48, -48) < (Math.max(cluster.obstructRange, cluster1.obstructRange))) {
                        toRemove.add(cluster1);
                    }
                }
            }
        }

        for (Cluster cluster : toRemove) {
            clusters.remove(cluster);
        }
    }
}
