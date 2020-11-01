package test.battleresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.battleresolver.MixedEngagement;
import unsw.gloriaromanus.units.Soldier;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MixedEngagementTest {

    Soldier u1;
    Soldier u2;
    NotSoRandom rng = new NotSoRandom();
    MixedEngagement eng = new MixedEngagement(rng);


    @BeforeEach
    public void init() {
        u1 = new Soldier();
        u2 = new Soldier();
        u1.setAttack(100);
        u1.setNumTroops(100);
        u2.setAttack(100);
        u2.setNumTroops(100);
        u1.setMorale(100);
        u2.setMorale(100);
    }
    @Test
    public void damageDealtIsFake() {
        assertEquals(0, new MixedEngagement().damageDealt(new Soldier(), new Soldier()));
    }

/*
    Not always true!
    @Test
    public void u1RangedSuffersCasualtiesInMelee() {
        u1.setRanged(true);
        u1.setAttack(0);
        u1.setArmour(0);
        rng.setNextDouble(0);
        assertTrue(eng.casualties(u1, u2)[0] > 0);
    }
*/
    @Test
    public void u1RangedDontSufferCasualtiesInRanged() {
        u1.setRanged(true);
        rng.setNextDouble(1);
        assertEquals(0,eng.casualties(u1, u2)[0]);
    }
/*
    Not always true!
    @Test
    public void u2RangedSuffersCasualtiesInMelee() {
        u2.setRanged(true);
        u2.setAttack(0);
        u2.setArmour(0);
        rng.setNextDouble(0);
        assertTrue(eng.casualties(u1, u2)[1] > 0);
    }
*/
    @Test
    public void u2RangedDontSufferCasualtiesInRanged() {
        u2.setRanged(true);
        rng.setNextDouble(1);
        assertEquals(0,eng.casualties(u1, u2)[1]);
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

}
