package project.unsw.gloriaromanus.game;

import project.unsw.gloriaromanus.battleresolver.BattleResolver;

public interface Campaign {
    public BattleResolver battleResolver = null;

    public void setBattleResolver (BattleResolver battleResolver);
    public BattleResolver getBattleResolver();

}
