package test.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.game.GameState;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
    GameState gs;
    Province rome;
    Faction player;
    Province dalmatia;
    Faction gaul;

    @BeforeEach
    public void init () {
        gs = new GameState();
        rome = new Province("rome", "rome");
        player = new Faction("rome");
        player.addProvince(rome);
        dalmatia = new Province("dalmatia", "gaul");
        gaul = new Faction("gaul");
        gaul.addProvince(dalmatia);
        gs.setFactionsFromArray(new Faction[]{player, gaul});
        gs.setPlayer("rome");
    }
    @Test
    public void AllGoalSuccess() {
        gs.setGoal(0); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.setTreasure(100001);
        player.addProvince(dalmatia);
        player.setWealth(400001);

        assertTrue(gs.winGoals());
    }
    @Test
    public void cAndTOrWGoalSuccess() {
        gs.setGoal(1); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.setTreasure(100001);
        player.addProvince(dalmatia);

        assertTrue(gs.winGoals());
    }
    @Test
    public void conquestGoalSuccess() {
        gs.setGoal(2); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        //Actually bad.
        player.addProvince(dalmatia);
        assertTrue(gs.winGoals());
    }
    @Test
    public void cOrTAndWGoalSuccess() {
        gs.setGoal(3); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.addProvince(dalmatia);
        player.setWealth(400001);

        assertTrue(gs.winGoals());
    }
    @Test
    public void wealthGoalSuccess() {
/*      // Old version, this was testing Faction's logic!!!
        rome.setTaxLevel(TaxLevel.LOW_TAX);
        gs.setGoal(4); // (c & t) || w, never true since no invasions
        assertFalse(gs.winGoals());

        int turnsNeeded = (400000 - rome.getWealth()) / 10;
        for (int i = 0; i < turnsNeeded + 1; i++) {
            rome.update(); //adds 20 wealth each time
        }
        player.update();
        assertTrue(gs.winGoals());
*/
        gs.setGoal(4); // (c & t) || w, never true since no invasions
        assertFalse(gs.winGoals());

        player.setWealth(400001);
        assertTrue(gs.winGoals());
    }
    @Test
    public void treasureGoalSuccess() {
        gs.setGoal(5); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.setTreasure(100001);
        assertTrue(gs.winGoals());
    }
    @Test
    public void cOrWAndTGoalSuccess() {
        gs.setGoal(6); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.setTreasure(100001);
        player.setWealth(400001);

        assertTrue(gs.winGoals());
    }
    @Test
    public void anyGoalSuccess() {
        gs.setGoal(7); // (c & w) || t, never true since no invasions
        assertFalse(gs.winGoals());

        player.setTreasure(100001);

        assertTrue(gs.winGoals());
    }
    //This is dependent on a config file, so it doesn't really make much
    // sense as unit test
    @Test
    public void getFactionsFromConfigFileTest () {
        Map<String, Faction> map = GameState.getFactionsFromConfigFile();
        assertNotNull(map);
        for (String k: map.keySet())
            assertEquals(k, map.get(k).getName());
    }
    @Test
    public void totalNumberProvincesTest() {
        assertEquals(2, gs.totalNumberProvinces());
    }
    @Test
    public void hasWonTest () {
        gs.setGoal(7); // (c & w) || t, never true since no invasions
        assertFalse(gs.hasWon());

        player.setTreasure(100001);

        assertTrue(gs.hasWon());

    }
    @Test
    public void hasWonAfterVictoryTest () {
        gs.setGoal(7); // (c & w) || t, never true since no invasions
        assertFalse(gs.hasWon());

        player.setTreasure(100001);

        assertTrue(gs.hasWon());

        assertFalse(gs.hasWon());
    }
    @Test
    public void initFactionsTest() {
        gs.initFactions();
        Map <String, Faction> map = gs.getFactions();
        assertNotNull(map);
        for (String k: map.keySet())
            assertEquals(k, map.get(k).getName());

    }
    @Test
    public void initArmiesTest () {
        gs.initArmies();
        assertEquals(2, rome.getArmy().getSize());
    }
    @Test
    public void initProvinceTest () {
        gs.initFactions();
        gs.initProvince();

        assertTrue(gs.getProvince("Britannia").getNeighbours().size() > 0);
    }
    @Test
    public void gameStateCreation () {
        gs = new GameState("Rome");
        assertEquals("Rome", gs.getPlayer());
        /*...*/
    }
    @Test
    public void goal0Readable () {
        gs.setGoal(0);
        assertEquals("Conquest AND Treasure AND Wealth", gs.goalReadable());
    }
    @Test
    public void goal1Readable () {
        gs.setGoal(1);
        assertEquals("Conquest AND (Treasure OR Wealth)", gs.goalReadable());
    }
    @Test
    public void goal2Readable () {
        gs.setGoal(2);
        assertEquals("Conquest OR (Treasure AND Wealth)", gs.goalReadable());
    }
    @Test
    public void goal3Readable () {
        gs.setGoal(3);
        assertEquals("(Conquest OR Treasure) AND Wealth", gs.goalReadable());
    }
    @Test
    public void goal4Readable () {
        gs.setGoal(4);
        assertEquals("(Conquest AND Treasure) OR Wealth", gs.goalReadable());
    }
    @Test
    public void goal5Readable () {
        gs.setGoal(5);
        assertEquals("(Conquest AND Wealth) OR Treasure", gs.goalReadable());
    }
    @Test
    public void goal6Readable () {
        gs.setGoal(6);
        assertEquals("(Conquest OR Wealth) AND Treasure", gs.goalReadable());
    }
    @Test
    public void goal7Readable () {
        gs.setGoal(7);
        assertEquals("Conquest OR Treasure OR Wealth", gs.goalReadable());
    }
}
