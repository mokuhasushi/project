package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

public class Game {
    public final Campaign campaign;
    public Faction [] factions;
    private final Faction player;
    public int turn;
    private final BattleResolver battleResolver;


    public Game(Campaign campaign, Faction player, BattleResolver battleResolver) {
        this.campaign = campaign;
        this.player = player;
        this.battleResolver = battleResolver;
    }

    public void invade (Province attacking, Province invaded, Army attacker) {
        attacking.removeTroops(attacker);
        BattleResult battleResult = battleResolver.battle(attacker, invaded.getArmy());
        switch (battleResult) {
            case ATTACKER_WON -> {
                invaded.conqueredBy(attacking.getOwner(), attacker);
                invaded.getOwner().removeProvince(invaded);
                attacking.getOwner().addProvince(invaded);
            }
            case ATTACKER_DEFEATED -> attacking.addTroops(attacker);
            case DRAW -> attacking.addTroops(attacker);
        }
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

    public void pass() {
        turn += 1;
        for (Province p : player.getProvinces()) {
            p.update();
            player.addWealth(p.getTaxRevenue());
        }
    }
}
