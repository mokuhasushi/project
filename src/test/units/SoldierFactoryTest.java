package test.units;

import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierFactory;
import unsw.gloriaromanus.units.SoldierType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoldierFactoryTest {

    @Test
    public void nameToSoldierMapIsCreatedCorrectlyForStandardSoldierFactory() {
        SoldierFactory sf = new SoldierFactory();
        Map<SoldierType, Soldier> map = sf.getNameToSoldier();
        for (SoldierType st: map.keySet()) {
            assertEquals(st.type, map.get(st).getName());
        }
    }
}
