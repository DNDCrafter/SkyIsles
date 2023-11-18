package structure;

import com.thelastflames.skyisles.chunk_generators.si.Point;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    List<Point>[] points;

    int width;
    int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        points = new List[width * height];
        for (int i = 0; i < points.length; i++) {
            points[i] = new ArrayList<>();
        }
    }
}
