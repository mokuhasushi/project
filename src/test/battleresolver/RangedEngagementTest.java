package test.battleresolver;

import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.battleresolver.RangedEngagement;
import unsw.gloriaromanus.units.Soldier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangedEngagementTest {
    @Test
    public void damageDealtIsPositive() {
        RangedEngagement rs = new RangedEngagement();
        Soldier attacker = new Soldier();
        attacker.setRanged(true);
        Soldier defender = new Soldier();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }

    @Test
    public void meleeDealsZeroDamage() {
        RangedEngagement rs = new RangedEngagement();
        Soldier attacker = new Soldier();
        Soldier defender = new Soldier();
        defender.setRanged(true);

        assertEquals(rs.damageDealt(attacker, defender), 0);
    }

    @Test
    public void meleeDealsZeroCasualties() {
        RangedEngagement rs = new RangedEngagement();
        Soldier u1 = new Soldier();
        Soldier u2 = new Soldier();
        u1.setRanged(true);

        assertEquals(0,rs.casualties(u1, u2)[0]);
    }
}
