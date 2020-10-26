package project.units;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import project.unsw.gloriaromanus.units.HeavyInfantry;

public class HeavyInfantryTest {
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }

    @Test
    public void blahTest2(){
        HeavyInfantry u = new HeavyInfantry();
        assertEquals( 50, u.getNumTroops());
    }
}
