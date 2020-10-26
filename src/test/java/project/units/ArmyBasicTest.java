package project.units;

import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.units.Army;
import project.unsw.gloriaromanus.units.HeavyInfantry;
import project.unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArmyBasicTest {
    @Test
    public void newArmySizeIsZeroTest() {
        Army a = new Army();
        assertEquals(a.getSize(), 0);
    }
    @Test
    public void newArmyFromArrayListHasCorrectSizeTest() {
        ArrayList<Unit> unitList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            unitList.add(new HeavyInfantry());
        }
        Army a = new Army(unitList);
        assertEquals(a.getSize(), 10);
    }

}
