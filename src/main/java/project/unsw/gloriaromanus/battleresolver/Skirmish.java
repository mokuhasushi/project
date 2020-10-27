package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

import static project.unsw.gloriaromanus.Utils.clamp;

/*
Skirmish Interface
Provides some static methods common to every instance.
The idea is to apply a Strategy Pattern to differentiate between melee and range
 */
public class Skirmish {
    private Unit u1;
    private Unit u2;
    private Engagement engagement;
    private int engageCounter;

    // Constructor instantiates engagement with the right instance
    // Strategy pattern
    public Skirmish (Unit u1, Unit u2) {
        this.u1 = u1;
        this.u2 = u2;

        if (u1.getRanged() != u2.getRanged()) {
            engagement = new MixedEngagement();
        }
        else if (u1.getRanged()) {
            engagement = new RangedEngagement();
        }
        else engagement = new MeleeEngagement();
    }

    //solves the engagement between two units
    public SkirmishResult solve (Unit u1, Unit u2) {
        return null;
    }

    //calculates the chances of the unit breaking
    static double breakChances(Unit u1, Unit u2, int casualties1, int casualties2) {
        double base = 100 - u1.getMorale()*10;
        double addition =
                ((double)casualties1 / u1.getNumTroops()) /
                ((double)casualties2 / u2.getNumTroops()) *
                        10.0;
        return clamp(5, base+addition, 100);
    }

    //calculates the chances of the unit fleeing
    static double fleeChances(Unit u1, Unit u2) {
        return clamp(10, 50 + 10 * (u1.getSpeed() - u2.getSpeed()), 100);
    }
}
