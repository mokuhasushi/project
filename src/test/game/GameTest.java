package test.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.game.GameState;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    Faction player;
    Province from;
    Province to;
    Game game;
    Army movingArmy;

    @BeforeEach
    public void init() {
        player = new Faction("player");
        from = new Province("roma", player);
        to = new Province("egypt", player);
        player.addProvince(from);
        player.addProvince(to);
        game = Game.getInstance(new GameState(), player);
        movingArmy = new Army();
    }
    @AfterEach
    public void clear() {
        game.clear();
    }

    @Test
    public void movingAnArmySimpleCase () {
        from.getArmy().addUnit(new Soldier());
        movingArmy.addUnit(from.getArmy().getUnit(0));
        game.move(from, to, movingArmy);

        assertEquals(movingArmy.getUnit(0), to.getArmy().getUnit(0));
        assertEquals(0,from.getArmy().getSize());
    }
    @Test
    public void movingOnlyPartOfAnArmyToAnotherArmy () {
        for (int i = 0; i < 10; i++) {
            from.getArmy().addUnit(new Soldier());
            to.getArmy().addUnit(new Soldier());
            if (i%2 == 0)
                movingArmy.addUnit(from.getArmy().getUnit(i));
        }

        game.move(from, to, movingArmy);

        assertEquals(5, from.getArmy().getSize());
        assertEquals(15, to.getArmy().getSize());
    }

    @Test
    public void setTaxes() {
        game.setTaxes(from, TaxLevel.HIGH_TAX);

        assertEquals(TaxLevel.HIGH_TAX, from.getTaxLevel());
    }

    @Test
    public void passingIncreasesPlayerWealth() {
        int w1 = player.getWealth();
        game.pass();
        assertTrue(w1 <= player.getWealth());
    }
    @Test
    public void passingIncreasesPlayerTreasure() {
        int w1 = player.getTreasure();
        game.pass();
        assertTrue(w1 < player.getTreasure());
    }

}
