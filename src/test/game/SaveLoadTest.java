package test.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.SaveLoad;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.world.Province;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveLoadTest {
    @Test
    public void simpleSaveLoadTestSoldier() {
        Soldier s_in = new Soldier();
        String filename = "src/test/resources/s1.json";
        SaveLoad.saveSoldier(s_in, filename);
        Soldier s_out = SaveLoad.loadSoldier(filename);
        assertEquals(s_in.getName(), s_out.getName());
    }
    @Test
    public void simpleSaveLoadTestArmy() {
        Army army_in = new Army();
        army_in.addUnit(new Soldier());
        army_in.addUnit(new Soldier());

        String filename = "src/test/resources/a1.json";
        SaveLoad.saveArmy(army_in, filename);

        Army army_out = SaveLoad.loadArmy(filename);
        assertEquals(army_in.getSize(), army_out.getSize());
        assertEquals(army_in.getUnit(0).getName(), army_out.getUnit(0).getName());
    }
    //Not working: TODO: refactor province to only store owner'string. Refactor game to be singleton
    //                  and have a map <String, Faction>
/*    @Test
    public void simpleSaveLoadTestProvince() {
        Army army = new Army();
        army.addUnit(new Soldier());
        army.addUnit(new Soldier());
        Faction player = new Faction("rome");
        Province province_in = new Province("rome", player);
        province_in.addTroops(army);

        String filename = "src/test/resources/p1.json";
        SaveLoad.saveProvince(province_in, filename);

        Province province_out = SaveLoad.loadProvince(filename);
        assertEquals(province_in.getOwner(), province_out.getOwner());
        assertEquals(province_in.getName(), province_out.getName());
        assertEquals(province_in.getTaxLevel(), province_out.getTaxLevel());
        assertEquals(province_in.getArmy().getSize(), province_out.getArmy().getSize());

    }*/
}
