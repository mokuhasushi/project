package test.battleresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
TODO:
Without dependency injection it is quite hard to test this class.
For the moment I will move onto other stuff, then come back and
think of something
 */
public class BattleResolverTest {
    BattleResolver battleResolver = new BattleResolver();
    Army attacker;
    Army defender;

    @BeforeEach
    public void init() {
        attacker = new Army();
        defender = new Army();
    }

    @Test
    public void ifDefenderHasEmptyArmyAttackerWon() {
        attacker.addUnit(new Soldier());

        BattleResult br =  battleResolver.battle(attacker, defender, null);

        assertEquals(BattleResult.ATTACKER_WON, br);
    }

    @Test
    public void aBattleBetweenBigArmiesEndsInDraw() {
        for (int i = 0; i < 201; i++) {
            attacker.addUnit(new Soldier());
            defender.addUnit(new Soldier());
        }
        BattleResult br = battleResolver.battle(attacker, defender, null);

        assertEquals(BattleResult.DRAW, br);
    }
    @Test
    public void whenDefenderDefeatedItsArmyIsEmpty() {
        for (int i = 0; i < 10; i++) {
            // with this setup it is (almost?) impossible to get a draw.
            // to test it properly should inject rng
            Soldier sd = new Soldier();
            sd.setAttack(0);
            sd.setName("soldier");
            Soldier sa = new Soldier();
            sa.setAttack(10);
            sa.setMorale(10);
            sa.setName("soldier");
            attacker.addUnit(sa);
            defender.addUnit(sd);
        }
        // To have result in terminal (and later on in javafx) uncomment
//        battleResolver.setTextReport(new BattleReporter());
        BattleResult br = battleResolver.battle(attacker, defender, null);

        assertEquals(BattleResult.ATTACKER_WON, br);
        assertEquals(10, attacker.getSize());
        assertTrue(defender.isDefeated());
    }
}
