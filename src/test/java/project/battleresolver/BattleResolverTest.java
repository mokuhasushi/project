package project.battleresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.unsw.gloriaromanus.battleresolver.BattleResolver;
import project.unsw.gloriaromanus.battleresolver.BattleResult;
import project.unsw.gloriaromanus.units.Army;
import project.unsw.gloriaromanus.units.Soldier;


import static org.junit.jupiter.api.Assertions.assertEquals;
/*
TODO:
Without dependency injection it is quite hard to test this class.
For the moment I will move onto other stuff, then come back and
think of something
 */
public class BattleResolverTest {
    BattleResolver battleResolver = BattleResolver.getInstance();
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

        BattleResult br =  battleResolver.battle(attacker, defender);

        assertEquals(BattleResult.ATTACKER_WON, br);
    }

    @Test
    public void aBattleBetweenBigArmiesEndsInDraw() {
        for (int i = 0; i < 201; i++) {
            attacker.addUnit(new Soldier());
            defender.addUnit(new Soldier());
        }
        BattleResult br = battleResolver.battle(attacker, defender);

        assertEquals(BattleResult.DRAW, br);
    }
}
