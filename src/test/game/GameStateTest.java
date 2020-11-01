package test.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.game.GameState;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        gs.setPlayer(player);
    }
    @Test
    public void wealthGoalSuccess() {
        rome.setTaxLevel(TaxLevel.LOW_TAX);
        gs.setGoal(4); // (c & t) || w, never true since no invasions
        assertFalse(gs.winGoals());

        int turnsNeeded = (400000 - rome.getWealth()) / 10;
        for (int i = 0; i < turnsNeeded + 1; i++) {
            rome.update(); //adds 20 wealth each time
        }
        player.updateWealth();
        assertTrue(gs.winGoals());

    }
}
