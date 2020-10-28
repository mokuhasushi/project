package battleresolver;

import org.junit.jupiter.api.Test;
import gloriaromanus.battleresolver.RangedEngagement;
import gloriaromanus.units.Archer;
import gloriaromanus.units.HeavyInfantry;
import gloriaromanus.units.Soldier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangedEngagementTest {
    @Test
    public void damageDealtIsPositive() {
        RangedEngagement rs = new RangedEngagement();
        Soldier attacker = new Archer();
        Soldier defender = new HeavyInfantry();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }

    @Test
    public void meleeDealsZeroDamage() {
        RangedEngagement rs = new RangedEngagement();
        Soldier attacker = new HeavyInfantry();
        Soldier defender = new Archer();

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
