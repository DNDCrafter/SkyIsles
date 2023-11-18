package structure;

import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import structure.test.StructurePotential;
import structure.test.StructureType;
import structure.test.TestRules;

import javax.swing.*;
import java.awt.*;

public class LayerTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(new JComponent() {
            @Override
            public void paint(Graphics g) {

                StructureType small = new StructureType(30) {
                    @Override
                    public boolean validate(PositionalRandomFactory factory, StructurePotential potential, int chunkX, int chunkZ) {
                        return false;
                    }

                    @Override
                    public StructureType decay(PositionalRandomFactory factory) {
                        return null;
                    }
                };

                TestRules rules = new TestRules(
                        new StructureType[]{
                                small
                        }
                );

                super.paint(g);
            }
        });
    }
}
