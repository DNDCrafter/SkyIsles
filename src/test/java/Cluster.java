import com.thelastflames.skyisles.chunk_generators.noise.NoiseWrapper;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    public double centerX;
    public double centerY;
    double obstructRange;
    double terrainRange;
    List<Point> pts = new ArrayList<>();
    boolean colossus = false;

    public Cluster(double obstructRange, double terrainRange) {
        this.terrainRange = terrainRange * 5;
        this.obstructRange = (obstructRange + this.terrainRange) * 2;
    }

    public double distTo(Point pt) {
        double dist = Double.POSITIVE_INFINITY;
        for (Point point : pts) {
            dist = Math.min(dist, point.distSquared(pt));
        }
        return Math.sqrt(dist);
    }

    public double distTo(Point pt, int offsetX, int offsetZ) {
        double dist = Double.POSITIVE_INFINITY;
        for (Point point : pts) {
            dist = Math.min(dist,
                    (pt.x - point.x - offsetX) * (pt.x - point.x - offsetX) +
                            (pt.y - point.y - offsetZ) * (pt.y - point.y - offsetZ)
            );
        }
        return Math.sqrt(dist);
    }

    public double distTo(Cluster other) {
        double dist = Double.POSITIVE_INFINITY;
        for (Point pt : other.pts) {
            dist = Math.min(dist, distTo(pt));
        }
        return dist;
    }

    public double distTo(Cluster other, int offsetX, int offsetZ) {
        double dist = Double.POSITIVE_INFINITY;
        for (Point pt : other.pts) {
            dist = Math.min(dist, distTo(pt, offsetX, offsetZ));
        }
        return dist;
    }

    double angleONearest(Point point) {
        if (colossus) {
            return Math.atan2(centerX - point.x, centerY - point.y);
        } else {
            double angle = 0;
            for (Point pt : pts) {
                angle += Math.atan2(pt.x - point.x, pt.y - point.y);
            }
            return angle / pts.size();
        }
    }

    public double sumDist(NoiseWrapper wrapper, Point point) {
        double v = 0;
        for (Point pt : pts) {
            v += 1 / (
                    ((point.x - pt.x) * (point.x - pt.x) +
                            (point.y - pt.y) * (point.y - pt.y))
            );
        }
        double m = 1d / pts.size();

//        if (wrapper != null) {
//            double ang = angleONearest(point) * 2 * Math.PI;
//
//            double x = Math.cos(ang);
//            double y = Math.sin(ang);
//            v -= wrapper.get(x / 10.0, y / 10.0) / 20.0;
//        }

        return v - m;
    }
}
