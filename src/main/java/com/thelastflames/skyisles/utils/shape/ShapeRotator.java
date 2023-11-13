package com.thelastflames.skyisles.utils.shape;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;

/**
 * This class assumes your shape is designed facing north
 */
public class ShapeRotator {
    public enum Transformation {
        LEFT,
        RIGHT,
        UP,
        DOWN;

        public AABB apply(AABB aabb) {
            switch (this) {
                case LEFT:
                    return new AABB(
                            1 - aabb.maxZ,
                            aabb.minY,
                            aabb.maxX,
                            1 - aabb.minZ,
                            aabb.maxY,
                            aabb.minX
                    );
                case RIGHT:
                    return new AABB(
                            aabb.maxZ,
                            aabb.minY,
                            1 - aabb.maxX,
                            aabb.minZ,
                            aabb.maxY,
                            1 - aabb.minX
                    );
            }
            throw new RuntimeException("NYI");
        }
    }

    public static VoxelShape transform(VoxelShape src, Transformation... transformations) {
        ArrayList<VoxelShape> boxes = new ArrayList<>();

        for (AABB aabb : src.toAabbs()) {
            for (Transformation transformation : transformations) {
                aabb = transformation.apply(aabb);
            }
            boxes.add(Shapes.create(aabb));
        }

        VoxelShape sp = Shapes.empty();
        for (VoxelShape box : boxes)
            sp = Shapes.joinUnoptimized(sp, box, BooleanOp.OR);
        return sp;
    }

    public static VoxelShape[] steps(VoxelShape defaultTop, Transformation... transformations) {
        VoxelShape curr = defaultTop;
        ArrayList<VoxelShape> res = new ArrayList<>();
        res.add(curr);
        for (Transformation transformation : transformations) {
            curr = transform(curr, transformations);
            res.add(curr);
        }
        return res.toArray(new VoxelShape[0]);
    }
}
