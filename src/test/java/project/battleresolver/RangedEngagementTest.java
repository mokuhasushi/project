package project.battleresolver;

import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.RangedEngagement;
import project.unsw.gloriaromanus.units.Archer;
import project.unsw.gloriaromanus.units.HeavyInfantry;
import project.unsw.gloriaromanus.units.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangedEngagementTest {
    @Test
    public void damageDealtIsPositive() {
        RangedEngagement rs = new RangedEngagement();
        Unit attacker = new Archer();
        Unit defender = new HeavyInfantry();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }

    @Test
    public void meleeDealsZeroDamage() {
        RangedEngagement rs = new RangedEngagement();
        Unit attacker = new HeavyInfantry();
        Unit defender = new Archer();

        assertEquals(rs.damageDealt(attacker, defender), 0);
    }

    @Test
    public void meleeDealsZeroCasualties() {
        RangedEngagement rs = new RangedEngagement();
        Unit u1 = new Unit();
        Unit u2 = new Unit();
        u1.setRanged(true);

        assertEquals(0,rs.casualties(u1, u2)[0]);
    }
}
