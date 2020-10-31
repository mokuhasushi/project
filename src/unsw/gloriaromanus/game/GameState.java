package unsw.gloriaromanus.game;

import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.world.Province;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GameState {
    private Map<String, Faction> factions;
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
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        player = new Faction("player");
    }
    public GameState(String player) {
        factions = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        init();
        this.player = factions.get(player);
    }

    private void init() {
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
}
