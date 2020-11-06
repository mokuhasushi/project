package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static Game instance = null;

    public GameState campaign;
    private Faction player;
    private BattleResolver battleResolver;

    private Game(GameState campaign) {
        this.campaign = campaign;
        this.player = campaign.getPlayerFaction();
        this.battleResolver = campaign.getBattleResolver();
    }
/*
    public static Game getInstance(GameState campaign, Faction player) {
        if (instance == null)
            instance = new Game(campaign, player);
        return instance;
    }
*/
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

    /**
     * The method to invade a Province from another, indicating the Army
     * @param attacking the Province which starts the attack
     * @param invaded the Province being invaded
     * @param attacker the Army of the attacker
     */
    public void invade (Province attacking, Province invaded, Army attacker) {
        attacking.removeTroops(attacker);
        BattleResult battleResult = battleResolver.battle(attacker, invaded.getArmy());
        Faction defender = getFaction(invaded.getOwner());
        switch (battleResult) {
            case ATTACKER_WON:
                defender.removeProvince(invaded);
                if(defender.getProvinces().size() == 0)
                    if (defender.equals(player))
                        gameLost();
                invaded.conqueredBy(attacking.getOwner(), attacker);
                getFaction(attacking.getOwner()).addProvince(invaded);
                changeOwnership(invaded, attacking.getOwner());
                break;
            case ATTACKER_DEFEATED:
                attacking.addTroops(attacker);
                break;
            case DRAW:
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
     * TODO: not checking anything
     * @param from Province
     * @param to Province
     * @param army Army
     */
    public void move (Province from, Province to, Army army) {
        from.removeTroops(army);
        to.moveTroops(army);
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
        campaign.addTurn();
        int totalWealth = 0;
        for (Province p : player.getProvinces()) {
            p.update();
            player.addTreasure(p.getTaxRevenue());
            totalWealth += p.getWealth();
        }
        player.setWealth(totalWealth);
        return false;
    }

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

    /**
     * Begins a new game, takes the player faction as a String as argument
     * @param player the Faction string id
     */
    public static void newGame(String player) {
        Game.clear();
        GameState gs = new GameState(player);

        Game.getInstance(gs);
    }

    public int getTurn() {
        return campaign.getTurn();
    }

    public int getPlayerGold() {
        return campaign.getPlayerFaction().getTreasure();
    }

    public int getPlayerWealth() {
        return campaign.getPlayerFaction().getWealth();
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

    /**
     *
     * @return Game information as a String
     */
    public String info() {
        return "Player faction: " + player.getName() + "\n" +
                "Factions in game: " + campaign.getFactions().keySet().toString() + "\n" +
                "Campaign goal: " + campaign.goalReadable();
    }
}
