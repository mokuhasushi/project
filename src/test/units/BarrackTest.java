package test.units;

import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.units.Barrack;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierFactory;

import static org.junit.jupiter.api.Assertions.*;

public class BarrackTest {
    StubSoldierFactory ssf = new StubSoldierFactory();

    @Test
    public void createSoldierSetsTurnRight() {
        Soldier s = new Soldier();
        ssf.setNextSoldier(s);

        Barrack b = new Barrack(ssf);
        assertTrue(b.createSoldier(""));
        assertEquals(1, b.getTurnToSlot1());
        assertEquals(0, b.getTurnToSlot2());
        assertNull(b.getSoldierSlot1());
    }
    @Test
    public void createSecondSoldierGoesInSecondSlot() {
        Soldier s = new Soldier();
        ssf.setNextSoldier(s);

        Barrack b = new Barrack(ssf);
        assertTrue(b.createSoldier(""));
        ssf.setNextSoldier(new Soldier());
        assertTrue(b.createSoldier(""));
        assertEquals(1, b.getTurnToSlot1());
        assertEquals(1, b.getTurnToSlot2());
        assertNull(b.getSoldierSlot1());
        assertNull(b.getSoldierSlot2());
    }
    @Test
    public void createThirdSoldierreturnsFalse() {
        Soldier s = new Soldier();
        ssf.setNextSoldier(s);

        Barrack b = new Barrack(ssf);
        assertTrue(b.createSoldier(""));
        ssf.setNextSoldier(new Soldier());
        assertTrue(b.createSoldier(""));
        ssf.setNextSoldier(new Soldier());
        assertFalse(b.createSoldier(""));
    }
    @Test
    public void createSoldierReturnsSoldierAfterRightNOfTurn() {
        Soldier s = new Soldier();
        ssf.setNextSoldier(s);

        Barrack b = new Barrack(ssf);
        assertTrue(b.createSoldier(""));
        assertEquals(1, b.getTurnToSlot1());
        assertEquals(0, b.getTurnToSlot2());
        assertNull(b.getSoldierSlot1());
        b.turnPassed();
        assertEquals(0, b.getTurnToSlot1());
        assertEquals(s, b.getSoldierSlot1());
        assertNull(b.getSoldierSlot1());
    }
    @Test
    public void createSoldierReturnsNullAfterMoreTurns() {
        Soldier s = new Soldier();
        ssf.setNextSoldier(s);

        Barrack b = new Barrack(ssf);
        assertTrue(b.createSoldier(""));
        assertEquals(1, b.getTurnToSlot1());
        assertEquals(0, b.getTurnToSlot2());
        assertNull(b.getSoldierSlot1());
        b.turnPassed();
        assertEquals(0, b.getTurnToSlot1());
        b.turnPassed();
        assertNull(b.getSoldierSlot1());
    }

    @Test
    public void soldiersOfDifferentTrainingTimeAreHandledCorrectly(){
        Soldier s1 = new Soldier();
        Soldier s2 = new Soldier();
        s2.setTrainingTime(2);
        Barrack b = new Barrack(ssf);

        ssf.setNextSoldier(s1);
        assertTrue(b.createSoldier(""));
        ssf.setNextSoldier(s2);
        assertTrue(b.createSoldier(""));

        assertEquals(1, b.getTurnToSlot1());
        assertEquals(2, b.getTurnToSlot2());
        assertNull(b.getSoldierSlot1());
        assertNull(b.getSoldierSlot2());
        b.turnPassed();
        assertEquals(0, b.getTurnToSlot1());
        assertEquals(1, b.getTurnToSlot2());
        assertEquals(s1, b.getSoldierSlot1());
        assertNull(b.getSoldierSlot2());
        b.turnPassed();
        assertEquals(0, b.getTurnToSlot2());
        assertEquals(s2, b.getSoldierSlot2());

    }

    class StubSoldierFactory extends SoldierFactory {
        private Soldier nextSoldier;
        void setNextSoldier(Soldier s){
            this.nextSoldier = s;
        }
        @Override
        public Soldier createSoldier(String soldier){
            return nextSoldier;
        }
    }
}
