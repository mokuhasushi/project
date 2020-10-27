package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

public interface Engagement {
    //TODO: Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties
    // (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).
    int damageDealt(Unit attacker, Unit defender);
    int[] casualties (Unit u1, Unit u2);
}
