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

public class SaveLoad {
    public static boolean saveSoldier(Soldier s, String filename) {
        File f = new File(filename);
        try {
            f.createNewFile();
            new ObjectMapper().writeValue(f,s);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static Soldier loadSoldier(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Soldier.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveArmy (Army army, String filename) {
        File f = new File(filename);
        try {
            f.createNewFile();
            new ObjectMapper().writeValue(f,army);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    public static boolean saveProvince (Province province, String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        File f = new File(filename);
        try {
            f.createNewFile();
            objectMapper.writeValue(f,province);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    public static boolean saveFaction (Faction faction, String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        File f = new File(filename);
        try {
            f.createNewFile();
            objectMapper.writeValue(f,faction);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    public static boolean saveGame (Game game, String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        File f = new File(filename);
        try {
            f.createNewFile();
            objectMapper.writeValue(f,game);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static Game loadGame(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new File(filename), Game.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}