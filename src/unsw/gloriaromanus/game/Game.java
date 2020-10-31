package unsw.gloriaromanus.game;

import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private static Game instance = null;

    public GameState campaign;
    private Faction player;
    private BattleResolver battleResolver;

    private Game(GameState campaign, Faction player) {
        this.campaign = campaign;
        this.player = player;
        this.battleResolver = campaign.getBattleResolver();
    }
    public static Game getInstance(GameState campaign, Faction player) {
        if (instance == null)
            instance = new Game(campaign, player);
        return instance;
    }
    public static Game getInstance(GameState campaign) {
        if (instance == null)
            instance = new Game(campaign, campaign.getPlayer());
        return instance;
    }
    public static Game getInstance () {
        return instance;
    }

    public static void clear(){
        instance = null;
    }

    public void invade (Province attacking, Province invaded, Army attacker) {
        attacking.removeTroops(attacker);
        BattleResult battleResult = battleResolver.battle(attacker, invaded.getArmy());
        Faction defender = getFaction(invaded.getOwner());
        switch (battleResult) {
            case ATTACKER_WON -> {
                defender.removeProvince(invaded);
                if(defender.getProvinces().size() == 0)
                    if (defender.equals(player))
                        gameLost();
                invaded.conqueredBy(attacking.getOwner(), attacker);
                getFaction(attacking.getOwner()).addProvince(invaded);

            }
            case ATTACKER_DEFEATED, DRAW -> attacking.addTroops(attacker);
        }
    }

    private void gameLost() {
        System.out.println("GAME LOST");
    }

    public void move (Province from, Province to, Army army) {
        from.removeTroops(army);
        to.moveTroops(army);
    }

    public void setTaxes(Province province, TaxLevel taxLevel) {
        province.setTaxLevel(taxLevel);
    }

    public void recruit(Province province, String soldier) {
        province.recruit(soldier);
    }

    public void setFactions (Faction[] factions) {
        campaign.setFactionsFromArray(factions);
    }

    public Faction getFaction (String faction) {
        return campaign.getFaction(faction);
    }

    public void removeFaction (String faction) {
        campaign.removeFaction(faction);
    }

    public void pass() {
        campaign.addTurn();
        int totalWealth = 0;
        for (Province p : player.getProvinces()) {
            p.update();
            player.addTreasure(p.getTaxRevenue());
            totalWealth += p.getWealth();
        }
        player.setWealth(totalWealth);
    }

    public void saveGame (String filename) {
        try {
            SaveLoad.saveGame(campaign, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean loadGame (String filename) {
        Game.clear();
        GameState gs = SaveLoad.loadGame(filename);
        if (gs == null)
            return false;
        Game.getInstance(gs, gs.getPlayer());
        return true;
    }

    public static void newGame(String player) {
        Game.clear();
        GameState gs = new GameState(player);

        Game.getInstance(gs);
    }

}
