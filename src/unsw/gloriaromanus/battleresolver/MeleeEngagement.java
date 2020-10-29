package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Soldier;

import java.util.Random;

import static unsw.gloriaromanus.Utils.clamp;

public class MeleeEngagement implements Engagement{
    Random rng;

    public MeleeEngagement() {
        rng = new Random();
    }

    //TODO: Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties
    // (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).
    @Override
    public int damageDealt(Soldier attacker, Soldier defender) {
        double result = (defender.getNumTroops() / 10.0) * (
                (double)attacker.getAttack() / (
                        defender.getArmour() +
                        defender.getShieldDefense() +
                        defender.getDefenseSkill()
                        )
                ) *
                (rng.nextGaussian() + 1);
        int damage = (int)Math.round(result);
        return clamp(0, damage, defender.getNumTroops());
    }

    @Override
    public int[] casualties(Soldier u1, Soldier u2) {
        return new int []{damageDealt(u2, u1), damageDealt(u1,u2)};
    }
}
