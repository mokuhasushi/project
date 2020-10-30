package test.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.game.GameState;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.world.Province;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameInvasionTest {
    Faction attacker;
    Province attacking;
    Faction defender;
    Province invaded;
    Game game;
    Army attackerArmy;
    Army defenderArmy;
    BattleResolverStub brs;

    @BeforeEach
    public void init() {
        attacker = new Faction("attacker");
        attacking = new Province("roma", attacker);
        defender = new Faction("defender");
        invaded = new Province("egypt", defender);
        attacker.addProvince(attacking);
        defender.addProvince(invaded);
        GameState c = new GameState();
        brs = new BattleResolverStub();
        c.setBattleResolver(brs);
        game = Game.getInstance(c, attacker);
        game.setFactions(new Faction[]{defender});
        attackerArmy = new Army();
        defenderArmy = new Army();
    }

    /*
    this feels like integration test actually
    @Test
    public void invasionOfAnEmptyProvinceSucceeds () {
        attackerArmy.addUnit(new Soldier());
        game.invade(attacking, invaded, attackerArmy);
        assertEquals(attacker, invaded.getOwner());
        assertEquals(1, attacker.getProvinces().size());
    }
 */

    @Test
    public void whenAttackerWinsProvinceAndArmyMoved () {
        attackerArmy.addUnit(new Soldier());
        brs.setBattleResult(BattleResult.ATTACKER_WON);
        game.invade(attacking, invaded, attackerArmy);
        assertEquals(attacker.getName(), invaded.getOwner());
        assertEquals(2, attacker.getProvinces().size());
        assertEquals(0, defender.getProvinces().size());
    }
    @Test
    public void whenAttackerDefeatedProvinceStaysOfDefender () {
        attackerArmy.addUnit(new Soldier());
        brs.setBattleResult(BattleResult.ATTACKER_DEFEATED);
        game.invade(attacking, invaded, attackerArmy);
        assertEquals(1, attacker.getProvinces().size());
        assertEquals(1, attackerArmy.getSize());
        assertEquals(defender.getName(), invaded.getOwner());
    }
    @Test
    public void whenDrawProvinceStaysOfDefender () {
        attackerArmy.addUnit(new Soldier());
        brs.setBattleResult(BattleResult.DRAW);
        game.invade(attacking, invaded, attackerArmy);
        assertEquals(1, attacker.getProvinces().size());
        assertEquals(1, attackerArmy.getSize());
        assertEquals(defender.getName(), invaded.getOwner());
    }

    private static class BattleResolverStub extends BattleResolver {
        static BattleResult br;
        @Override
        public BattleResult battle(Army attacker, Army defender) {
            return br;
        }
        public void setBattleResult(BattleResult br) {
            this.br = br;
        }
    }
}
