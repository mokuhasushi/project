package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

import java.util.Random;

public class MeleeSkirmish implements Skirmish{
    Random rng;

    public MeleeSkirmish() {
        rng = new Random();
    }

    @Override
    public SkirmishResult solve(Unit u1, Unit u2) {
        return null;
    }

    //TODO: Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties
    // (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).
    @Override
    public int damageDealt(Unit attacker, Unit defender) {
        double result = (defender.getNumTroops() / 10.0) * (
                (double)attacker.getAttack() / (
                        defender.getArmour() +
                        defender.getShieldDefense() +
                        defender.getDefenseSkill()
                        )
                ) *
                (rng.nextGaussian() + 1);
        int damage = Math.min((int)Math.round(result),defender.getNumTroops());
        return Math.max(0, damage);
    }
}
