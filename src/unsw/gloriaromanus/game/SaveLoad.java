package unsw.gloriaromanus.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.world.Province;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class SaveLoad {
    public static void saveSoldier(Soldier s, String filename) throws IOException {
        File f = new File(filename);
            f.createNewFile();
            new ObjectMapper().writeValue(f,s);
    }
    public static Soldier loadSoldier(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Soldier.class);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static void saveArmy (Army army, String filename) throws IOException {
        File f = new File(filename);
            f.createNewFile();
            new ObjectMapper().writeValue(f,army);
    }
    public static Army loadArmy(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Army.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveProvince (Province province, String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File f = new File(filename);
            f.createNewFile();
            objectMapper.writeValue(f,province);
    }
    public static Province loadProvince(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Province.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveFaction (Faction faction, String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File f = new File(filename);
            f.createNewFile();
            objectMapper.writeValue(f,faction);
    }
    public static Faction loadFaction(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Faction.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveGame (GameState game, String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        File f = new File(filename);
            f.createNewFile();
            objectMapper.writeValue(f,game);
    }
    public static GameState loadGame(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), GameState.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}