package project.units;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import project.unsw.gloriaromanus.units.Unit;

public class UnitTest {
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }

    @Test
    public void blahTest2(){
        Unit u = new Unit();
        assertEquals(u.getNumTroops(), 50);
    }
}
