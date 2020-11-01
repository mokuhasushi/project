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
    private Faction player;

    private int goal;
    @JsonIgnore private BattleResolver battleResolver;
    private int turn;
    private boolean won = false;

    public Map<String, Faction> getFactions() {
        return factions;
    }

    public void setFactions(Map<String, Faction> factions) {
        this.factions = factions;
        provincesToOwner = new HashMap<>();
        for (Faction f: factions.values())
            for (Province p: f.getProvinces())
                provincesToOwner.put(p.getName(), f.getName());
    }

    public void setPlayer(Faction player) {
        this.player = player;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

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
    public void provincesToOwnerFromFactions () {
        provincesToOwner = new HashMap<>();
        for (Faction f: factions.values())
            for (Province p: f.getProvinces())
                provincesToOwner.put(p.getName(), f.getName());
    }

    public GameState() {
        factions = new HashMap<>();
        provincesToOwner = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        battleResolver.setTextReport(new BattleReporter());
        turn = 0;
        player = new Faction("player");
        this.goal = new Random().nextInt(7);
    }

    //This one should be called!
    public GameState(String player) {
        factions = new HashMap<>();
        provincesToOwner = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        battleResolver.setTextReport(new BattleReporter());
        turn = 0;
        initFactions();
        initProvince();
        initArmies();
        this.player = factions.get(player);
        this.goal = new Random().nextInt(7);
    }


    public GameState(String player, Faction [] factions) {
        this.factions = new HashMap<>();
        provincesToOwner = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        setFactionsFromArray(factions);
        this.player = this.factions.get(player);
        this.goal = new Random().nextInt(7);
    }

    private void initArmies() {
        for (String province: provincesToOwner.keySet()) {
            Province p = getProvince(province);
            Army army = new Army();
            SoldierFactory sf = new SoldierFactory(p.getOwner());
            army.addUnit(sf.createSoldier(SoldierType.MELEE_INFANTRY));
            army.addUnit(sf.createSoldier(SoldierType.RANGED_INFANTRY));
            p.addTroops(army);
        }
    }

    private void initProvince() {
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


    private void initFactions() {
        Map<String, Faction> factions = getFactionsFromConfigFile();
        if (factions == null) {
            System.err.println("Something went wrong in newGame initialization!");
            return;
        }
        this.setFactions(factions);

    }
    //From GloriaRomanusController, adapted
    private static Map<String, Faction> getFactionsFromConfigFile() {
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
        if (faction.equals(player.getName()))
            return player;
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

    public void setFactionsFromArray(Faction[] factions) {
        this.factions = new HashMap<>();
        for (Faction f: factions) {
            this.factions.put(f.getName(), f);
        }
        provincesToOwnerFromFactions();
    }

    public Faction getPlayer() {
        return player;
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
        return player.getProvinces().size() == provincesToOwner.size();
    }
    public boolean treasureGoal() {
        return player.getTreasure() > 100000;
    }
    public boolean wealthGoal() {
        return player.getWealth() > 400000;
    }
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

    public void changeOwnership(Province invaded, String owner) {
        provincesToOwner.replace(invaded.getName(), owner);
    }
}
