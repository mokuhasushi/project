package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;

public class Campaign {
    public BattleResolver battleResolver = null;

    public Campaign () {
        battleResolver = BattleResolver.getInstance();
    }
    public void setBattleResolver (BattleResolver battleResolver) {
        this.battleResolver = battleResolver;
    }
    public BattleResolver getBattleResolver() {
        return battleResolver;
    }

    public boolean getGoals() {
        return false;
    }
}
