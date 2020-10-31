package test.game;

import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.game.BattleReporter;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleReporterTest {
    @Test
    public void itWorksMoreToBeAddedLater() {
        Army a1 = new Army();
        Army a2 = new Army();
        a1.addUnit(new Soldier());
        a2.addUnit(new Soldier());
        a1.addUnit(new Soldier());
        a2.addUnit(new Soldier());
        a1.addUnit(new Soldier());
        a2.addUnit(new Soldier());

        OutputStream out = new ByteArrayOutputStream();

        BattleResolver battleResolver = BattleResolver.getInstance();
        battleResolver.setTextReport(new BattleReporter(new PrintStream(out)));

        battleResolver.battle(a1, a2);

        assertEquals("Battle begun" ,out.toString().split("!")[0]);
    }
}
