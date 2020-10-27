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
        MeleeEngagement me = new MeleeEngagement();
        Unit attacker = new HeavyInfantry();
        Unit defender = new HeavyInfantry();

        assertTrue(me.damageDealt(attacker, defender) >= 0);
    }

    @Test
    public void casualtiesAreLessThenActualUnits() {
        MeleeEngagement me = new MeleeEngagement();
        Unit u1 = new Unit();
        Unit u2 = new Unit();
        u1.setNumTroops(3);
        u2.setNumTroops(10);

        int [] cs = me.casualties(u1, u2);
        assertTrue(cs[0] <= u1.getNumTroops());
        assertTrue(cs[1] <= u2.getNumTroops());
    }

}
