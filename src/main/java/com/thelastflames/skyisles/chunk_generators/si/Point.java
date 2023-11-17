package com.thelastflames.skyisles.chunk_generators.si;

public class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt(
                (x) * (x) +
                        (y) * (y)
        );
    }

    public double distance(Point currentPt) {
        return Math.sqrt(
                (x - currentPt.x) * (x - currentPt.x) +
                        (y - currentPt.y) * (y - currentPt.y)
        );
    }

    public double distSquared(Point currentPt) {
        return
                (x - currentPt.x) * (x - currentPt.x) +
                        (y - currentPt.y) * (y - currentPt.y);
    }

    public void setDistance(Point closest, double v) {
        x -= closest.x;
        y -= closest.y;
        double len = length();
        x /= len;
        y /= len;
        x *= v;
        y *= v;

        x += closest.x;
        y += closest.y;
    }
}
