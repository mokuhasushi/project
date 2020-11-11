package unsw.gloriaromanus.game;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.SoldierFactory;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class GameState {
    private Map<String, Faction> factions;
    private static Map<String, String> provincesToOwner;
    private String player;

    private int goal;
    @JsonIgnore private BattleResolver battleResolver;
    private int turn;
    private boolean won = false;

    /**
     * for JSON serialization and tests
     */
    public GameState() {};

    //This one should be called!
    public GameState(String player) {
        factions = new HashMap<>();
        provincesToOwner = new HashMap<>();
        battleResolver = new BattleResolver();
        turn = 0;
        initFactions();
        initProvince();
        initArmies();
        this.player = player;
        this.goal = new Random().nextInt(7);
    }


    /**
     * For testing purposes
     * @param player String
     * @param factions Array Faction
     */
    public GameState(String player, Faction [] factions) {
        this.factions = new HashMap<>();
        provincesToOwner = new HashMap<>();
        battleResolver = new BattleResolver();
        turn = 0;
        setFactionsFromArray(factions);
        this.player = player;
        this.goal = new Random().nextInt(7);
    }


    public Map<String, Faction> getFactions() {
        return factions;
    }

    /**
     * Set the factions from a Map, and updates also provinceToOwner
     * @param factions Map
     */
    public void setFactions(Map<String, Faction> factions) {
        this.factions = factions;
        provincesToOwner = new HashMap<>();
        for (Faction f: factions.values())
            for (Province p: f.getProvinces())
                provincesToOwner.put(p.getName(), f.getName());
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Once the game has been won, if the player decides to keep playing,
     * it must not be presented again the Victory screen
     * @return won
     */
    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public Map<String, String> getProvincesToOwner() {
        return provincesToOwner;
    }

    public void setProvincesToOwner(Map<String, String> provincesToOwner) {
        GameState.provincesToOwner = provincesToOwner;
    }

    /**
     * updates probincesToOwner according to the factions Map
     */
    public void provincesToOwnerFromFactions () {
        provincesToOwner = new HashMap<>();
        for (Faction f: factions.values())
            for (Province p: f.getProvinces())
                provincesToOwner.put(p.getName(), f.getName());
    }


    /**
     * Starts each province with two units.
     * This can be avoided by creating a config file with all provinces
     * TODO
     */
    public void initArmies() {
        for (String province: provincesToOwner.keySet()) {
            Province p = getProvince(province);
            Army army = new Army();
            SoldierFactory sf = new SoldierFactory(p.getOwner());
            army.addUnit(sf.createSoldier(SoldierType.MELEE_INFANTRY));
            army.addUnit(sf.createSoldier(SoldierType.RANGED_INFANTRY));
            p.addTroops(army);
        }
    }

    /**
     * reads from config file all the adjacencies of Provinces and initialize them
     */
    public void initProvince() {
        String content = null;
        try {
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject provinceAdjacencyMatrix = new JSONObject(content);
        for (String k: provinceAdjacencyMatrix.keySet()) {
            Province p = factions.get(provincesToOwner.get(k)).getProvince(k);
            for (String n: provinceAdjacencyMatrix.getJSONObject(k).keySet()) {
                if (provinceAdjacencyMatrix.getJSONObject(k).getBoolean(n))
                    p.addNeighbour(n);
            }
        }
    }


    public void initFactions() {
        Map<String, Faction> factions = getFactionsFromConfigFile();
        if (factions == null) {
            System.err.println("Something went wrong in newGame initialization!");
            return;
        }
        this.setFactions(factions);

    }

    /**
     * From GloriaRomanusController, adapted
     * Reads a json with the initial province ownerships and initializes the factions
     * @return a map containing the factions
     */
    public static Map<String, Faction> getFactionsFromConfigFile() {
        String content = null;
        try {
            content = Files.readString(Paths.get(
                    "src/unsw/gloriaromanus/initial_province_ownership.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject ownership = new JSONObject(content);
        Map<String, Faction> m = new HashMap<String, Faction>();
        for (String key : ownership.keySet()) {
            // key will be the faction name
            JSONArray ja = ownership.getJSONArray(key);
            // value is province name
            Faction f = new Faction(key);
            for (int i = 0; i < ja.length(); i++) {
                String value = ja.getString(i);
                f.addProvince(new Province(value, key));
                provincesToOwner.put(value, key);
            }
            m.put(key, f);
        }
        return m;
    }

    public void setBattleResolver (BattleResolver battleResolver) {
        this.battleResolver = battleResolver;
    }
    public BattleResolver getBattleResolver() {
        return battleResolver;
    }

    public int totalNumberProvinces() {
        return provincesToOwner.size();
    }

    public Faction getFaction(String faction) {
        if (faction.equals(player))
            return factions.get(player);
        return factions.get(faction);
    }

    public void removeFaction(String faction) {
        factions.remove(faction);
    }

    public int getTurn() {
        return turn;
    }

    public void addTurn() {
        turn += 1;
    }

    /**
     * This is only used for test setup
     * @param factions array of Faction
     */
    public void setFactionsFromArray(Faction[] factions) {
        this.factions = new HashMap<>();
        for (Faction f: factions) {
            this.factions.put(f.getName(), f);
        }
        provincesToOwnerFromFactions();
    }

    /**
     * for Json serializer
     * @return String
     */
    public String getPlayer() {
        return player;
    }
    public Faction getPlayerFaction() {
        return factions.get(player);
    }

    public Province getProvince(String province) {
        return factions.get(provincesToOwner.get(province)).getProvince(province);
    }
    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
    public boolean conquestGoal() {
        return factions.get(player).getProvinces().size() == provincesToOwner.size();
    }
    public boolean treasureGoal() {
        return factions.get(player).getTreasure() > 100000;
    }
    public boolean wealthGoal() {
        return factions.get(player).getWealth() > 400000;
    }

    /**
     * Since there are only 8 win conditions possible, it makes sense to hard code them
     * @return if the player has won
     */
    public boolean winGoals() {
        switch (goal) {
            case 0:
                return conquestGoal() && treasureGoal() && wealthGoal();
            case 1:
                return conquestGoal() && (treasureGoal() || wealthGoal());
            case 2:
                return conquestGoal() || (treasureGoal() && wealthGoal());
            case 3:
                return (conquestGoal() || treasureGoal()) && wealthGoal();
            case 4:
                return (conquestGoal() && treasureGoal()) || wealthGoal();
            case 5:
                return (conquestGoal() && wealthGoal()) || treasureGoal();
            case 6:
                return (conquestGoal() || wealthGoal()) && treasureGoal();
            case 7:
                return conquestGoal() || treasureGoal() || wealthGoal();
            default:
                return false;
        }
    }

    /**
     * If the player has already won, it must return always false.
     * @return true if won and never won before
     */
    public boolean hasWon () {
        if (winGoals() && !won){
            won = true;
            return true;
        }
        return false;
    }
    public String goalReadable () {
        String c = "Conquest";
        String t = "Treasure";
        String w = "Wealth";
        switch (goal) {
            case 0:
                return c + " AND " + t + " AND " + w;
            case 1:
                return c + " AND " + "(" + t + " OR " + w + ")";
            case 2:
                return c + " OR " + "(" + t + " AND " + w + ")";
            case 3:
                return "(" + c + " OR " + t + ")" + " AND " + w;
            case 4:
                return "(" + c + " AND " + t + ")" + " OR " + w;
            case 5:
                return "(" + c + " AND " + w + ")" + " OR " + t;
            case 6:
                return "(" + c + " OR " + w + ")" + " AND " + t;
            case 7:
                return c + " OR " + t + " OR " + w;
            default:
                return "Invalid goal!";
        }
    }

    /**
     * Updates provinceToOwner with a new owner for invaded Province
     * @param invaded Province
     * @param owner String
     */
    public void changeOwnership(Province invaded, String owner) {
        provincesToOwner.replace(invaded.getName(), owner);
    }

    public Faction getFactionFromProvince(String province) {
        return getFaction(provincesToOwner.get(province));
    }

}
