package project.battleresolver;

import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.MeleeEngagement;
import project.unsw.gloriaromanus.units.Archer;
import project.unsw.gloriaromanus.units.HeavyInfantry;
import project.unsw.gloriaromanus.units.Unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeleeEngagementTest {
    @Test
    public void damageDealtIsPositive() {
        MeleeEngagement rs = new MeleeEngagement();
        Unit attacker = new Archer();
        Unit defender = new HeavyInfantry();

        assertTrue(rs.damageDealt(attacker, defender) >= 0);
    }

}
