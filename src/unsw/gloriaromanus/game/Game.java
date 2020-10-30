package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

public class Game {
    private static Game instance = null;

    public Campaign campaign;
    public Faction [] factions;
    private Faction player;
    public int turn;
    private BattleResolver battleResolver;



    private Game(Campaign campaign, Faction player) {
        this.campaign = campaign;
        this.player = player;
        this.battleResolver = campaign.getBattleResolver();
        this.turn = 0;
    }
    public static Game getInstance(Campaign campaign, Faction player) {
        if (instance == null)
            instance = new Game(campaign, player);
        return instance;
    }
    public Game getInstance () {
        return instance;
    }

    public void invade (Province attacking, Province invaded, Army attacker) {
        attacking.removeTroops(attacker);
        BattleResult battleResult = battleResolver.battle(attacker, invaded.getArmy());
        Faction defender = invaded.getOwner();
        switch (battleResult) {
            case ATTACKER_WON -> {
                defender.removeProvince(invaded);
                if(defender.getProvinces().size() == 0)
                    if (defender.equals(player))
                        gameLost();
                invaded.conqueredBy(attacking.getOwner(), attacker);
                attacking.getOwner().addProvince(invaded);

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

    public void clear(){
        instance = null;

    }

    public void pass() {
        turn += 1;
        int totalWealth = 0;
        for (Province p : player.getProvinces()) {
            p.update();
            player.addTreasure(p.getTaxRevenue());
            totalWealth += p.getWealth();
        }
        player.setWealth(totalWealth);
    }
}
