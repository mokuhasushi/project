package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;
import unsw.gloriaromanus.battleresolver.BattleResult;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.world.Province;

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
}
