package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;

public class GameState {
    private boolean conquestGoal;
    private boolean treasureGoal;
    private boolean wealthGoal;
    private BattleResolver battleResolver = null;
    private int totalNumberProvinces = 50;

    public GameState() {
        battleResolver = BattleResolver.getInstance();
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
}
