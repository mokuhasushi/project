package test.game;

import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.game.GameState;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.SaveLoad;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.world.Province;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SaveLoadTest {
    @Test
    public void simpleSaveLoadTestSoldier() {
        Soldier s_in = new Soldier();
        String filename = "src/test/resources/s1.json";
        try {
            SaveLoad.saveSoldier(s_in, filename);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
        Soldier s_out = SaveLoad.loadSoldier(filename);
        assertEquals(s_in.getName(), s_out.getName());
    }
    @Test
    public void loadingInvalidSoldierFileReturnsNull () {
        assertNull(SaveLoad.loadSoldier("notAFile"));
    }
    @Test
    public void simpleSaveLoadTestArmy() {
        Army army_in = new Army();
        army_in.addUnit(new Soldier());
        army_in.addUnit(new Soldier());

        String filename = "src/test/resources/a1.json";
        try {
            SaveLoad.saveArmy(army_in, filename);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }

        Army army_out = SaveLoad.loadArmy(filename);
        assertEquals(army_in.getSize(), army_out.getSize());
        assertEquals(army_in.getUnit(0).getName(), army_out.getUnit(0).getName());
    }
    @Test
    public void loadingInvalidArmyFileReturnsNull () {
        assertNull(SaveLoad.loadArmy("notAFile"));
    }
    @Test
    public void simpleSaveLoadTestProvince() {
        Army army = new Army();
        army.addUnit(new Soldier());
        army.addUnit(new Soldier());
//        Faction player = new Faction("rome");
        Province province_in = new Province("rome", "player");
        province_in.addTroops(army);

        String filename = "src/test/resources/p1.json";
        try {
            SaveLoad.saveProvince(province_in, filename);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }

        Province province_out = SaveLoad.loadProvince(filename);
        assertEquals(province_in.getOwner(), province_out.getOwner());
        assertEquals(province_in.getName(), province_out.getName());
        assertEquals(province_in.getTaxLevel(), province_out.getTaxLevel());
        assertEquals(province_in.getArmy().getSize(), province_out.getArmy().getSize());

    }
    @Test
    public void loadingInvalidProvinceFileReturnsNull () {
        assertNull(SaveLoad.loadProvince("notAFile"));
    }
    @Test
    public void simpleSaveLoadTestFaction() {
        Army army = new Army();
        army.addUnit(new Soldier());
        army.addUnit(new Soldier());
        Province province = new Province("rome", "player");
        province.addTroops(army);
        Faction faction_in = new Faction("player");
        faction_in.addProvince(province);

        String filename = "src/test/resources/f1.json";
        try {
            SaveLoad.saveFaction(faction_in, filename);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }

        Faction faction_out = SaveLoad.loadFaction(filename);
        assertEquals(faction_in.getName(), faction_out.getName());
        assertEquals(faction_in.getProvinces().get(0).getName(), faction_out.getProvinces().get(0).getName());
        assertEquals(faction_in.getProvinces().get(0).getArmy().getSize(), faction_out.getProvinces().get(0).getArmy().getSize());
    }
    @Test
    public void loadingInvalidFactionFileReturnsNull () {
        assertNull(SaveLoad.loadFaction("notAFile"));
    }
    @Test
    public void simpleSaveLoadTestGame() {
        Army army = new Army();
        army.addUnit(new Soldier());
        army.addUnit(new Soldier());
        Province province = new Province("rome", "player");
        province.addTroops(army);
        Faction faction = new Faction("player");
        faction.addProvince(province);
        Faction gaul = new Faction("gaul");
        gaul.addProvince(new Province("lyon","gaul"));

        GameState gameState_in = new GameState();

        gameState_in.setFactionsFromArray(new Faction[] {faction, gaul, new Faction("egypt")});
        gameState_in.setPlayer(faction.getName());

        String filename = "src/test/resources/g1.json";
        try {
            SaveLoad.saveGame(gameState_in, filename);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }

        GameState gameState_out = SaveLoad.loadGame(filename);
        assertEquals(gameState_in.getTurn(), gameState_out.getTurn());
        assertEquals(gameState_in.getFaction("player").getName(), gameState_out.getFaction("player").getName());
        assertEquals(gameState_in.getFaction("gaul").getName(), gameState_out.getFaction("gaul").getName());
        assertEquals(gameState_in.getFaction("gaul").getProvinces().get(0), gameState_out.getFaction("gaul").getProvinces().get(0));
        assertEquals(gameState_in.getProvince("lyon").getNeighbours().size(), gameState_out.getProvince("lyon").getNeighbours().size());
    }
    @Test
    public void loadingInvalidGameFileReturnsNull () {
        assertNull(SaveLoad.loadGame("notAFile"));
    }
}
