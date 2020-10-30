package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;

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

    public GameState() {
        factions = new HashMap<>();
        battleResolver = BattleResolver.getInstance();
        turn = 0;
        player = new Faction("player");
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
        for (int i = 0; i < factions.size(); i++) {
            System.out.println(factions.values().toArray()[i]);
        }
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

    public void setFactions(Faction[] factions) {
        for (Faction f: factions) {
            this.factions.put(f.getName(), f);
        }
    }
}
