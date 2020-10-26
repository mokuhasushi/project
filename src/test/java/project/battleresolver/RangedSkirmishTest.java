package project.battleresolver;

import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.RangedSkirmish;
import project.unsw.gloriaromanus.units.Archer;
import project.unsw.gloriaromanus.units.HeavyInfantry;
import project.unsw.gloriaromanus.units.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangedSkirmishTest {
    @Test
    public void damageDealtIsPositive() {
        RangedSkirmish rs = new RangedSkirmish();
        Unit attacker = new Archer();
        Unit defender = new HeavyInfantry();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }

    @Test void meleeDealsZeroDamage() {
        RangedSkirmish rs = new RangedSkirmish();
        Unit attacker = new HeavyInfantry();
        Unit defender = new Archer();

        assertEquals(rs.damageDealt(attacker, defender), 0);
    }
}
