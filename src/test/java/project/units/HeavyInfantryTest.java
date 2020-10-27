package project.units;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import project.unsw.gloriaromanus.units.HeavyInfantry;

public class HeavyInfantryTest {
    @Test
    public void heavyInfantryHas50StartingTroops(){
        HeavyInfantry u = new HeavyInfantry();
        assertEquals( 50, u.getNumTroops());
    }
    @Test
    public void addingTroopsWorksAsExpected() {
        HeavyInfantry u = new HeavyInfantry();
        u.addTroops(10);
        assertEquals(60, u.getNumTroops());
    }
    @Test
    public void removingTroopsWorksAsExpected() {
        HeavyInfantry u = new HeavyInfantry();
        u.removeTroops(10);
        assertEquals(40, u.getNumTroops());
    }
    @Test
    public void removingMoreTroopsThenPresentWorksAsExpected() {
        HeavyInfantry u = new HeavyInfantry();
        u.removeTroops(60);
        assertEquals(0, u.getNumTroops());
    }
}
