package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Game {
    public final int ONE_PROVINCE_TRAVERSED = 4;

    private static Game instance = null;

    public GameState campaign;
    private Faction player;
    private BattleResolver battleResolver;
    private ArrayList <AI> ais;

    private PropertyChangeSupport support;

    //for multiplayer
    private int playerCounter;

    private Game(GameState campaign) {
        this.campaign = campaign;
        this.player = campaign.getPlayerFaction();
        this.battleResolver = campaign.getBattleResolver();
        this.ais = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
        playerCounter = 0;
        player.update();
    }

    public static Game getInstance(GameState campaign) {
        if (instance == null)
            instance = new Game(campaign);
        return instance;
    }
    public static Game getInstance () {
        return instance;
    }

    public static void clear(){
        instance = null;
    }

    public void addReporter (PropertyChangeListener reporter) {
        support.addPropertyChangeListener(reporter);
    }
    /**
     * The method to invade a Province from another, indicating the Army
     * @param attacking the Province which starts the attack
     * @param invaded the Province being invaded
     * @param attacker the Army of the attacker
     */
    public void invade (Province attacking, Province invaded, Army attacker) {
        if (attacking.isJustConquered()){
            support.firePropertyChange("message",
                    null, attacking.getName() + " has just been conquered");
            return;
        }
        if (attacker.getMovement() < ONE_PROVINCE_TRAVERSED){
            support.firePropertyChange("message", null, "Not enough movement!");
            return;}
        support.firePropertyChange("factions", null, new String[]{attacking.getOwner(), invaded.getOwner()});
        support.firePropertyChange("message", null, "Battle for " + invaded.getName() +" begun!");
        attacking.removeTroops(attacker);
        attacker.moved(ONE_PROVINCE_TRAVERSED);
        BattleResult battleResult = battleResolver.battle(attacker, invaded.getArmy(),
                support.getPropertyChangeListeners().length > 0 ? support.getPropertyChangeListeners()[0] : null);
        Faction defender = getFaction(invaded.getOwner());
        switch (battleResult) {
            case ATTACKER_WON:
                support.firePropertyChange("message", null, attacking.getOwner()+" won!");
                defender.removeProvince(invaded);
                if(defender.getProvinces().size() == 0)
                    if (defender.equals(player))
                        gameLost();
                invaded.conqueredBy(getFactionFromProvince(attacking.getName()), attacker);
                getFaction(attacking.getOwner()).addProvince(invaded);
                changeOwnership(invaded, attacking.getOwner());
                break;
            case ATTACKER_DEFEATED:
                support.firePropertyChange("message", null, attacking.getOwner() + " lost!");
                attacking.addTroops(attacker);
                break;
            case DRAW:
                support.firePropertyChange("message", null,
                        "The battle ended in a draw!");
                attacking.addTroops(attacker);
                break;
        }
    }

    /**
     * Method to update the GameState after a Province is conquered
     * @param invaded the invaded Province
     * @param owner the new owner
     */
    private void changeOwnership(Province invaded, String owner) {
        campaign.changeOwnership(invaded, owner);
    }

    /**
     * Game Lost Method
     */
    private void gameLost() {
        System.out.println("GAME LOST");
    }

    /**
     * Method to move an army from one Province to another
     * @param from Province
     * @param to Province
     * @param army Army
     */
    public void move (Province from, Province to, Army army) {
        if (from.isJustConquered()){
            support.firePropertyChange("message",
                    null, from.getName() + " has just been conquered!");
            return;
        }
        if (army.getMovement() < ONE_PROVINCE_TRAVERSED) {
            support.firePropertyChange("message", null, "Not enough movement!");
            return;
        }
        from.removeTroops(army);
        army.moved(ONE_PROVINCE_TRAVERSED);
        to.addTroops(army);
    }

    /**
     * Set the taxes of a particular Province
     * @param province Province
     * @param taxLevel TaxLevel
     */
    public void setTaxes(Province province, TaxLevel taxLevel) {
        province.setTaxLevel(taxLevel);
    }

    /**
     * Set the factions in the game from an array in GameState
     * I think this is beign used only for setup in tests
     * @param factions array of Faction
     */
    public void setFactions (Faction[] factions) {
        campaign.setFactionsFromArray(factions);
    }

    //TODO check
    public String [] getFactions () {return campaign.getFactions().keySet().toArray(new String[0]);}

    public Faction getFaction (String faction) {
        return campaign.getFaction(faction);
    }

    public void removeFaction (String faction) {
        campaign.removeFaction(faction);
    }

    /**
     * Pass a turn
     * @return true if the player has won
     */
    public boolean pass() {
        if (campaign.hasWon()){
            saveGame("autosave.save");
            return true;}

        playerCounter = playerCounter + 1;
        if (playerCounter < campaign.getPlayers().length){
            campaign.setPlayer(campaign.getPlayers()[playerCounter]);
            player = getFaction(campaign.getPlayer());
            player.update();
            return false;
        }else {
        playerCounter %= campaign.getPlayers().length;
        for (AI ai: ais) {
            ai.playTurn();
        }
        campaign.addTurn();
        return false;
    }}

    /**
     * Save the game
     * @param filename String
     * @return true if no exception were raised
     */
    public boolean saveGame (String filename) {
        try {
            SaveLoad.saveGame(campaign, filename);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Save the game
     * @param file File
     * @return true if no exception were raised
     */
    public boolean saveGame (File file) {
        try {
            SaveLoad.saveGame(campaign, file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Load a game
     * @param filename String
     * @return true if loading was successful
     */
    public static boolean loadGame (String filename) {
        Game.clear();
        GameState gs = SaveLoad.loadGame(filename);
        if (gs == null)
            return false;
        Game.getInstance(gs);
        return true;
    }
    public static boolean loadGame (File file) {
        Game.clear();
        GameState gs = SaveLoad.loadGame(file.getName());
        if (gs == null)
            return false;
        Game.getInstance(gs);
        return true;
    }

    /**
     * Begins a new game, takes the player faction as a String as argument
     * @param player the Faction string id
     */
    public static void newGame(String player) {
        Game.clear();
        GameState gs = new GameState(player);

        Game.getInstance(gs).initAI();
    }
    public static void newGame(String [] players) {
        Game.clear();
        GameState gs = new GameState(players);

        Game.getInstance(gs).initAI();
    }

    public int getTurn() {
        return campaign.getTurn();
    }

    public int getPlayerGold() {
        return player.getTreasure();
    }

    public int getPlayerWealth() {
        return player.getWealth();
    }
    public Faction getPlayer () {
        return player;
    }
    public ArrayList <Province> getPlayerProvinces () {
        return player.getProvinces();
    }
    public Province getProvince(String province) {
        return campaign.getProvince(province);
    }

    /**
     * This was implemented for the textual version of the game.
     * Probably not needed
     * @param p1 From
     * @param p2 To
     */
    public void moveOrInvade(Province p1, Province p2) {
        if (p1.getOwner().equals(p2.getOwner()))
            this.move(p1, p2, p1.getArmy());
        else
            this.invade(p1, p2, p1.getArmy());
    }
    public void moveOrInvade(Province p1, Province p2, Army army) {
        if (p1.getOwner().equals(p2.getOwner()))
            this.move(p1, p2, army);
        else
            this.invade(p1, p2, army);
    }

    /**
     * Recruits a unit if the faction can afford the cost and there are available
     * slots. For design by contract no check is made about province's owner being faction
     * @param province Province
     * @param soldierType SoldierType
     * @return true if enough money
     */
//    public boolean recruit (Faction faction, Province province, SoldierType soldierType) {
    public boolean recruit (Province province, SoldierType soldierType) {
        Faction faction = getFaction(province.getOwner());
        int cost = faction.getSoldierFactory().createSoldier(soldierType).getCost();
        if (cost < faction.getTreasure()){
            faction.addTreasure(-cost);
            return province.recruit(soldierType);}
        return false;
    }

    /*
     * Method to initialize AIs. At the moment very straightforward
     */
    public void initAI () {
        Set <String> aiFactions = new HashSet<>(campaign.getFactions().keySet());
        aiFactions.removeAll(Arrays.asList(campaign.getPlayers()));

        for (String k: aiFactions)
            if (!k.equals(player.getName()))
                ais.add(new AI(k));
    }

    /**
     *
     * @return Game information as a String
     */
    public String info() {
        return "Player faction: " + player.getName() + "\n" +
                "Factions in game: " + campaign.getFactions().keySet().toString() + "\n" +
                "Campaign goal: " + campaign.goalReadable();
    }

    public Faction getFactionFromProvince(String province) {
        return campaign.getFactionFromProvince(province);
    }
}
