package gloriaromanus.battleresolver;

import gloriaromanus.units.Soldier;

public interface Engagement {
    //TODO: Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties
    // (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).
    int damageDealt(Soldier attacker, Soldier defender);
    int[] casualties (Soldier u1, Soldier u2);
}
