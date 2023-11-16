import com.thelastflames.skyisles.chunk_generators.noise.NoiseWrapper;
import com.thelastflames.skyisles.chunk_generators.noise.SINoiseSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Random;

public class GeneratorTest {
    protected static double sampleDensity(NoiseWrapper density, NoiseWrapper negDensity, int wx, int wy) {
        double scl = 1;
        double dV = density.get(wx / 100d * scl, wy / 100d * scl) + 0.5;
        double dnV = negDensity.get(wx / 1000d * scl, wy / 1000d * scl) + 0.5;
        dV *= 1.3;
        dV = Math.pow(dV, 2);
        dV -= dnV;
        dV *= dnV;
        dV = Math.min(1, dV);
        dV = Math.max(0, dV);
        return dV;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        SINoiseSettings settings = new SINoiseSettings(
                0, "simplex_amplitudes",
                (List<Integer>) List.of(1, 1, 2, 1, 2, 0, 1, 0),
                1, 1, 1, 1
        );
//        SINoiseSettings settingsDensity = new SINoiseSettings(
//                0, "perlin_simplex",
//                (List<Integer>) List.of(1, -1, -1),
//                1, 1, 1, 1
//        );
//        SINoiseSettings negativeDensity = new SINoiseSettings(
//                38472, "simplex",
//                (List<Integer>) List.of(0),
//                1, 1, 1, 1
//        );
        SINoiseSettings settingsDensity = new SINoiseSettings(
                97435, "perlin_simplex",
                (List<Integer>) List.of(1, -1, -1, -2),
                1, 1, 1, 1
        );
        SINoiseSettings negativeDensity = new SINoiseSettings(
                38472, "perlin",
                (List<Integer>) List.of(1, -1, 2, 1, 2),
                1, 1, 1, 1
        );

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == ' ') frame.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.add(new JComponent() {
            @Override
            public void paint(Graphics g) {

                Cell cell00 = Cell.of(-1, -1);
                Cell cell01 = Cell.of(-1, 0);
                Cell cell02 = Cell.of(-1, 1);

                Cell cell10 = Cell.of(0, -1);
                Cell cell11 = Cell.of(0, 0);
                Cell cell12 = Cell.of(0, 1);

                Cell cell20 = Cell.of(1, -1);
                Cell cell21 = Cell.of(1, 0);
                Cell cell22 = Cell.of(1, 1);

                XoroshiroRandomSource src = new XoroshiroRandomSource(new Random().nextLong());
                PositionalRandomFactory factory = new XoroshiroRandomSource.XoroshiroPositionalRandomFactory(src.nextLong(), src.nextLong());

                NoiseWrapper wrapper = settings.create(factory.at(0, 0, 0).nextLong());
                NoiseWrapper density = settingsDensity.create(factory.at(0, 0, 0).nextLong());
                NoiseWrapper negDensity = negativeDensity.create(factory.at(0, 0, 0).nextLong());

                cell00.distrobute(factory);
                cell01.distrobute(factory);
                cell02.distrobute(factory);

                cell10.distrobute(factory);
                cell11.distrobute(factory);
                cell12.distrobute(factory);

                cell20.distrobute(factory);
                cell21.distrobute(factory);
                cell22.distrobute(factory);

                cell11.trim(
                        cell20, cell21, cell22,
                        cell10, /*   */ cell12,
                        cell00, cell01, cell02
                );

                int displaySize = 350;

                g.drawRect(displaySize, displaySize, displaySize, displaySize);

                for (int x = -displaySize; x < displaySize * 2; x++) {
                    for (int y = -displaySize; y < displaySize * 2; y++) {

//                        int wx = (int) ((x / (double) displaySize) * (48 * 16));
//                        int wy = (int) ((y / (double) displaySize) * (48 * 16));

                        //@formatter:off
//                        double dV = sampleDensity(density, negDensity, wx, wy);
//                        if (dV == 1) {g.setColor(new Color((int) (Math.min(1, Math.max(dV, 0)) * 255), 0, 0)); } else if (dV > 0.5) { g.setColor(new Color(0, (int) (Math.min(1, Math.max(dV, 0)) * 255), 0)); } else if (dV > 0.25) { g.setColor(new Color(0, 0, (int) (Math.min(1, Math.max(dV, 0)) * 255))); } else { g.setColor(Color.BLACK); }
//                        g.fillRect(displaySize + x, displaySize + y, 1, 1);
                        //@formatter:on

                        Cell[][] cells = new Cell[][]{
                                {cell00, cell01, cell02},
                                {cell10, cell11, cell12},
                                {cell20, cell21, cell22},
                        };

                        for (int celY = 0; celY < cells.length; celY++) {
                            Cell[] row = cells[celY];
                            for (int celX = 0; celX < row.length; celX++) {
                                Cell cell = row[celX];

                                int wx = (int) ((x / (double) displaySize) * (48 * 16));
                                int wy = (int) ((y / (double) displaySize) * (48 * 16));

                                for (Cluster cluster : cell.clusters) {
                                    double dist = cluster.sumDist(wrapper, new Point(
                                            wx / 16d,
                                            wy / 16d
                                    ));

                                    double d = dist / cluster.terrainRange / 1.5;

                                    if (d > 0) {
                                        d *= 2;
                                        if (d < 0) d = 0;
                                        if (d > 1) d = 1;
                                        d = 1 - d;
                                        d = Math.pow(d, 100);
                                        d = 1 - d;
                                        if (d > 0.5) {
                                            d = (d - 0.5) / 2 + 0.5;
                                        }

                                        double nv = (wrapper.get(wx / 1000d, wy / 1000d) + 0.5);
                                        if (nv < 0.25) nv = 0.25;
                                        d = nv * d;

                                        g.setColor(new Color(
//                                                (int) (Math.min(1, Math.max(d, 0)) * 255),
                                                0,
                                                (int) (Math.min(1, Math.max(d, 0)) * 255),
//                                                (int) (Math.min(1, Math.max(d, 0)) * 255)
                                                0
                                        ));
                                        g.fillRect(celX * displaySize + x, celY * displaySize + y, 1, 1);
                                    }
                                }
                            }
                        }

                    }
                }

                g.setColor(new Color(0));
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
