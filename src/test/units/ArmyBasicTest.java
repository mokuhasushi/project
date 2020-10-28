package units;

import org.junit.jupiter.api.Test;
import gloriaromanus.units.Army;
import gloriaromanus.units.HeavyInfantry;
import gloriaromanus.units.Soldier;

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
        ArrayList<Soldier> soldierList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            soldierList.add(new HeavyInfantry());
        }
        Army a = new Army(soldierList);
        assertEquals(a.getSize(), 10);
    }
}
