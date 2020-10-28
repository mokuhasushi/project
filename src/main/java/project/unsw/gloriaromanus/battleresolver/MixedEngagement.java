package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Soldier;

import java.util.Random;

import static project.unsw.gloriaromanus.Utils.clamp;

public class MixedEngagement implements Engagement{
    private RangedEngagement re = new RangedEngagement();
    private MeleeEngagement me = new MeleeEngagement();
    private Random rng;

    public MixedEngagement() {
        rng = new Random();
    }

    // This will never be called
    @Override
    public int damageDealt(Soldier attacker, Soldier defender) {
        return 0;
    }

    /*
    First, depending on which unit is melee and which is ranged calculate threshold
    then if nextDouble (which returns a number 0 <= x <= 1) is < threshold melee, else ranged
     */
    @Override
    public int[] casualties(Soldier u1, Soldier u2) {
        double threshold;
        if (u1.isRanged()) {
            threshold = (50 + 10.0 * (u2.getSpeed() - u1.getSpeed())) / 100;
        }
        else
        {
            threshold = (50 + 10.0 * (u1.getSpeed() - u2.getSpeed())) / 100;
        }
        threshold = clamp(0.05, threshold, 0.95);
        if (rng.nextDouble() < threshold){
            return me.casualties(u1,u2);
        }
        else {
            return re.casualties(u1,u2);
        }
    }
}

