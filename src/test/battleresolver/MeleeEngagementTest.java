package battleresolver;

import org.junit.jupiter.api.Test;
import gloriaromanus.battleresolver.MeleeEngagement;
import gloriaromanus.units.HeavyInfantry;
import gloriaromanus.units.Soldier;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeleeEngagementTest {
    @Test
    public void damageDealtIsPositive() {
        MeleeEngagement me = new MeleeEngagement();
        Soldier attacker = new HeavyInfantry();
        Soldier defender = new HeavyInfantry();

        assertTrue(me.damageDealt(attacker, defender) >= 0);
    }

    @Test
    public void casualtiesAreLessThenActualUnits() {
        MeleeEngagement me = new MeleeEngagement();
        Soldier u1 = new Soldier();
        Soldier u2 = new Soldier();
        u1.setNumTroops(3);
        u2.setNumTroops(10);

        int [] cs = me.casualties(u1, u2);
        assertTrue(cs[0] <= u1.getNumTroops());
        assertTrue(cs[1] <= u2.getNumTroops());
    }

}
