package unsw.gloriaromanus.game;

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

public class GameState {
    private Map<String, Faction> factions;
    private static Map<String, Province> provinces;
    private Faction player;
    private boolean conquestGoal;
    private boolean treasureGoal;
    private boolean wealthGoal;
    private BattleResolver battleResolver = null;
    private int totalNumberProvinces = 50;
    private int turn;

    public Map<String, Faction> getFactions() {
        return factions;
    }

    public void setFactions(Map<String, Faction> factions) {
        this.factions = factions;
    }

    public GameState() {
        factions = new HashMap<>();
        provinces = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        player = new Faction("player");
    }

    //This one should be called!
    public GameState(String player) {
        factions = new HashMap<>();
        provinces = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        initProvince();
        initFactions();
        initArmies();
        battleResolver.setTextReport(new BattleReporter());
        this.player = factions.get(player);
    }


    public GameState(String player, Faction [] factions) {
        this.factions = new HashMap<>();
        provinces = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        setFactionsFromArray(factions);
        this.player = this.factions.get(player);
    }

    private void initArmies() {
        for (Province p: provinces.values()) {
            Army army = new Army();
            SoldierFactory sf = new SoldierFactory(p.getOwner());
            army.addUnit(sf.createSoldier(SoldierType.MELEE_INFANTRY));
            army.addUnit(sf.createSoldier(SoldierType.RANGED_INFANTRY));
            p.addTroops(army);
        }
    }

    private static void initProvince() {
        String content = null;
        try {
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject provinceAdjacencyMatrix = new JSONObject(content);
        Iterator keys = provinceAdjacencyMatrix.keys();
        for (String k: provinceAdjacencyMatrix.keySet()) {
            Province p = new Province(k, "");
            for (String n: provinceAdjacencyMatrix.getJSONObject(k).keySet()) {
                if (provinceAdjacencyMatrix.getJSONObject(k).getBoolean(n))
                    p.addNeighbour(n);
            }
            provinces.put(k, p);
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
                f.addProvince(GameState.provinces.get(value));
                GameState.provinces.get(value).setOwner(key);
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

    //TODO, not so trivial
    public boolean isVictory(Faction player) {
        boolean win = true;
        if (conquestGoal)
            win &= playerHasAllTerritories(player);
        return win;
    }

    private boolean playerHasAllTerritories(Faction player) {
        return player.getProvinces().size() == totalNumberProvinces;
    }

    public int getTotalNumberProvinces() {
        return totalNumberProvinces;
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
        for (Faction f: factions) {
            this.factions.put(f.getName(), f);
        }
    }

    public Faction getPlayer() {
        return player;
    }

    public Province getProvince(String province) {
        return provinces.get(province);
    }
}
