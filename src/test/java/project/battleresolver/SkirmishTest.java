package project.battleresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.*;
import project.unsw.gloriaromanus.units.Unit;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SkirmishTest {

    static Unit u1;
    static Unit u2;
    static NotSoRandom rng;
    static StubEngagement eng;
//    static Skirmish s;

    @BeforeEach
    public void init() {
        u1 = new Unit();
        u2 = new Unit();
        rng = new NotSoRandom();
        eng = new StubEngagement();
/*
        Skirmish s = new Skirmish(u1, u2, rng);
*/
    }

    /*
    A much slower unit (s2-s1 > 4) should always have at least 10% chances
     */
    @Test
    public void fleeingChancesRangeLowerBoundTest() {
        u1.setSpeed(1);
        u2.setSpeed(100);

        assertEquals(0.1, Skirmish.fleeChances(u1, u2));
    }
    @Test
    public void fleeingChancesRangeUpperBoundTest() {
        u1.setSpeed(100);
        u2.setSpeed(1);

        assertEquals(1, Skirmish.fleeChances(u1, u2));
    }

    @Test
    public void breakChancesRangeLowerBound() {
        u1.setMorale(10);
        u1.setNumTroops(1);
        u2.setNumTroops(1);
        assertEquals(0.05, Skirmish.breakChances(u1, u2, 0,1));
    }
    @Test
    public void breakChancesRangeUpperBound() {
        u1.setMorale(00);
        u1.setNumTroops(1);
        u2.setNumTroops(1);
        assertEquals(1.0, Skirmish.breakChances(u1, u2, 10,1));
    }
    @Test
    public void breakChancesZeroDivision() {
        u1.setMorale(5); // base = 100 - 5*10 = 50
        u1.setNumTroops(1); // addition = 1/1 /
        u2.setNumTroops(1); //            0/1 (-> 1)
                            //            * 10
        assertEquals(0.6, Skirmish.breakChances(u1, u2, 1,0));
    }

    @Test
    public void solveForBrokenUnit1FleeSuccess() {
        u1.setBroken(true);
        u2.setBroken(false);
        u1.setSpeed(1);
        u2.setSpeed(1);
        rng.setNextDouble(0); // Always a success

        Skirmish s = new Skirmish(u1, u2, rng, eng);

        SkirmishReport sr = s.solve();

        assertEquals(SkirmishResult.U1_FLEE, sr.getResult());
        assertTrue(sr.getU1().isBroken());

    }
    @Test
    public void solveForBrokenUnit1FleeFailureAndDefeted() {
        u1.setBroken(true);
        u2.setBroken(false);
        u1.setSpeed(1);
        u2.setSpeed(1);
        u1.setNumTroops(1);
        rng.setNextDouble(1.1); // Always a failure
        eng.setCasualties(1, 1);

        Skirmish s = new Skirmish(u1, u2, rng, eng);

        SkirmishReport sr = s.solve();
        assertEquals(SkirmishResult.U1_DEFEAT, sr.getResult());
        assertTrue(sr.getU1().isDefeated());

    }
    @Test
    public void solveForBrokenUnit2FleeSuccess() {
        u1.setBroken(false);
        u2.setBroken(true);
        u1.setSpeed(1);
        u2.setSpeed(1);
        rng.setNextDouble(0); // Always a success

        Skirmish s = new Skirmish(u1, u2, rng, eng);

        SkirmishReport sr = s.solve();
        assertEquals(SkirmishResult.U2_FLEE, sr.getResult());
        assertTrue(sr.getU2().isBroken());

    }
    @Test
    public void solveForBrokenUnit2FleeFailureAndDefeted() {
        u1.setBroken(false);
        u2.setBroken(true);
        u1.setSpeed(1);
        u2.setSpeed(1);
        u1.setNumTroops(1);
        rng.setNextDouble(1.1); // Always a failure
        eng.setCasualties(1, 1);

        Skirmish s = new Skirmish(u1, u2, rng, eng);

        SkirmishReport sr = s.solve();
        assertEquals(SkirmishResult.U2_DEFEAT, sr.getResult());
        assertTrue(sr.getU2().isDefeated());
    }
    @Test
    public void solveForBrokenUnits() {
        u1.setBroken(true);
        u2.setBroken(true);

        Skirmish s = new Skirmish(u1, u2);

        SkirmishReport sr = s.solve();
        assertEquals(SkirmishResult.DRAW_FLED, sr.getResult());
        assertTrue(sr.getU1().isBroken());
    }
    @Test
    public void solveForCombatU1breaksAndFlees() {
        u1.setBroken(false);
        u2.setBroken(false);
        u1.setSpeed(1);
        u2.setSpeed(1);
        u1.setNumTroops(2);
        rng.setNextDouble(0); // U1 breaks
        rng.setNextDouble(1.1); //U2 doesn't
        rng.setNextDouble(0); //U1 flees

        eng.setCasualties(1, 0);

        Skirmish s = new Skirmish(u1, u2, rng, eng);
        SkirmishReport er = s.solve();
        assertEquals(SkirmishResult.U1_FLEE, er.getResult());
        assertEquals(2, er.getNumEngagements());
        assertTrue(er.getU1().isBroken());
    }
    @Test
    public void solveForCombatU1DoesntBreaksAndIsDefeated() {
        rng.setNextDouble(1.1); // U1 doesn't break
        rng.setNextDouble(0); //U2 breaks but not defeated

        eng.setCasualties(1, 0);

        Skirmish s = new Skirmish(u1, u2, rng, eng);
        SkirmishReport er = s.solve();
        assertEquals(SkirmishResult.U1_DEFEAT, er.getResult());
        assertTrue(er.getU1().isDefeated());
        assertTrue(er.getU2().isBroken());
    }
    @Test
    public void solveForCombatU2breaksAndIsDefeated() {
        u1.setNumTroops(2);
        rng.setNextDouble(1.1); // U1 doesn't break
        rng.setNextDouble(0); //U2 breaks and is defeated

        eng.setCasualties(1, 1);

        Skirmish s = new Skirmish(u1, u2, rng, eng);
        SkirmishReport er = s.solve();
        assertEquals(SkirmishResult.U2_DEFEAT, er.getResult());
        assertTrue(er.getU2().isDefeated());
    }
    @Test
    public void solveForCombatU1AndU2Defeated() {
        rng.setNextDouble(1.1); // U1 doesn't break
        rng.setNextDouble(1.1); // U2 doesn't break

        eng.setCasualties(1, 1);

        Skirmish s = new Skirmish(u1, u2, rng, eng);
        SkirmishReport er = s.solve();
        assertEquals(SkirmishResult.DRAW_DEFEATED, er.getResult());
        assertTrue(er.getU1().isDefeated());
        assertTrue(er.getU2().isDefeated());
    }

    static class NotSoRandom extends Random {
        LinkedList<Double> nextDoubles;
        NotSoRandom () {
            nextDoubles = new LinkedList<>();
        }
        @Override
        public double nextDouble() {
            return nextDoubles.remove();
        }
        void setNextDouble(double d) {
            nextDoubles.add(d);
        }
    }

    static class StubEngagement implements Engagement {

        int c1;
        int c2;

        public void setCasualties(int c1, int c2) {
            this.c1 = c1;
            this.c2 = c2;
        }
        @Override
        public int damageDealt(Unit attacker, Unit defender) {
            return 0;
        }

        @Override
        public int[] casualties(Unit u1, Unit u2) {
            return new int[]{c1, c2};
        }
    }
}
