package project.battleresolver;

import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.MeleeSkirmish;
import project.unsw.gloriaromanus.units.Archer;
import project.unsw.gloriaromanus.units.HeavyInfantry;
import project.unsw.gloriaromanus.units.Unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeleeSkirmishTest {
    @Test
    public void damageDealtIsPositive() {
        MeleeSkirmish rs = new MeleeSkirmish();
        Unit attacker = new Archer();
        Unit defender = new HeavyInfantry();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }


}
