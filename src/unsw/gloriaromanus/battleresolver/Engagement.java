package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Soldier;

public interface Engagement {
    /**
     * Method to calculate the casualties inflicted by the unit attacker
     *                                              to the unit defender
     * @param attacker Soldier
     * @param defender Soldier
     * @return an int, the casualties inflicted
     */
    int damageDealt(Soldier attacker, Soldier defender);

    /**
     * Method to calculate the casualties in both direction
     * @param u1 Soldier
     * @param u2 Soldier
     * @return an int[] of size 2, containing the casualties for [u1, u2]
     */
    int[] casualties (Soldier u1, Soldier u2);
}
