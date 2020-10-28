package unsw.gloriaromanus.game;

import unsw.gloriaromanus.battleresolver.BattleResolver;

public interface Campaign {
    public BattleResolver battleResolver = null;

    public void setBattleResolver (BattleResolver battleResolver);
    public BattleResolver getBattleResolver();

}
