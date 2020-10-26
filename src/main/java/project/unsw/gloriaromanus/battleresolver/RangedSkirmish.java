package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

import java.util.Random;

public class RangedSkirmish implements Skirmish{
    Random rng;

    public RangedSkirmish() {rng = new Random();}

    @Override
    public SkirmishResult solve(Unit u1, Unit u2) {

        return null;
    }

    //TODO: You should ensure the ranged attack damage above incorporates the effect of
    // any bonuses/penalties (e.g. the 10% loss of missile attack damage from fire arrows).
    public int damageDealt (Unit attacker, Unit defender) {
        if (attacker.getRanged()){
            return 0;
        }
        double result = (defender.getNumTroops() / 10.0) * (
                (double)(attacker.getAttack()) / (defender.getArmour() + defender.getShieldDefense())
                ) *
                (rng.nextGaussian() + 1);
        int damage = Math.min((int) Math.round(result), defender.getNumTroops());
        return Math.max(0, damage);
    }

}
