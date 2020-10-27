package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

import java.util.Random;

import static project.unsw.gloriaromanus.Utils.clamp;

public class RangedEngagement implements Engagement{
    Random rng;

    public RangedEngagement() {rng = new Random();}

    //TODO: You should ensure the ranged attack damage above incorporates the effect of
    // any bonuses/penalties (e.g. the 10% loss of missile attack damage from fire arrows).
    public int damageDealt (Unit attacker, Unit defender) {
        if (!attacker.getRanged()){
            return 0;
        }
        double result = (defender.getNumTroops() / 10.0) * (
                (double)(attacker.getAttack()) / (defender.getArmour() + defender.getShieldDefense())
                ) *
                (rng.nextGaussian() + 1);
        int damage = (int) Math.round(result);
        return clamp(0,damage, defender.getNumTroops());
    }

    @Override
    public int[] casualties(Unit u1, Unit u2) {
        return new int []{damageDealt(u1,u2), damageDealt(u2, u1)};
    }

}
