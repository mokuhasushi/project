package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

import java.util.Random;

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
    private int engageCounter = 1;
    private Random rng;

    // Constructor instantiates engagement with the right instance
    // Strategy pattern
    public Skirmish (Unit u1, Unit u2) {
        this.u1 = u1;
        this.u2 = u2;
        rng = new Random();

        if (u1.isRanged() != u2.isRanged()) {
            engagement = new MixedEngagement();
        }
        else if (u1.isRanged()) {
            engagement = new RangedEngagement();
        }
        else engagement = new MeleeEngagement();
    }

    /*
     This constructor is for testing. I could introduce some testing framework
     (thinking of GUICE), but maybe it is best to keep it simple
     */
    public Skirmish (Unit u1, Unit u2, Random rng, Engagement eng) {
        this.u1 = u1;
        this.u2 = u2;
        this.rng = rng;
        this.engagement = eng;
    }

    //solves the engagement between two units
    public EngagementReport solve () {
        while (true) {
            if (u1.isBroken() && !u2.isBroken()){
                if (rng.nextDouble() < fleeChances(u1, u2)) {
                    return new EngagementReport(u1, u2, SkirmishResult.U1_FLEE, engageCounter);
                }
                else {
                    /*
                    If the broken unit doesn't route, it will suffer casualties without
                    damaging the other unit.
                    engagement.casualties() returns an Array of size 2, the first entry
                    relative to casualties for u1, the second for u2
                     */
                    u1.removeTroops(engagement.casualties(u1,u2)[0]);
                }
            }
            else if (!u1.isBroken() && u2.isBroken()){
                if (rng.nextDouble() < fleeChances(u2, u1)) {
                    return new EngagementReport(u1, u2, SkirmishResult.U2_FLEE, engageCounter);
                }
                else {
                    u2.removeTroops(engagement.casualties(u1,u2)[1]);
                }
            }
            else if (u1.isBroken() && u2.isBroken()){
                return new EngagementReport(u1,u2,SkirmishResult.DRAW, engageCounter);
            }
            else{
                /*
                Combat case.
                From the specification I got that a unit can break even if the other unit is defeated.
                TODO: check if it is the intended behavior
                 https://github.com/indexie/project#breaking-a-unit
                 */
                int [] casualties = engagement.casualties(u1, u2);
                if (breakChances(u1, u2, casualties[0],casualties[1]) > rng.nextDouble())
                    u1.setBroken(true);
                if (breakChances(u2, u1, casualties[1],casualties[0]) > rng.nextDouble())
                    u2.setBroken(true);
                u1.removeTroops(casualties[0]);
                u2.removeTroops(casualties[1]);
            }
            if (u1.isDefeated() && u2.isDefeated()) {
                // From the spec is not clear, I assume both defeated is a draw
                return new EngagementReport(u1,u2,SkirmishResult.DRAW, engageCounter);
            }
            else if (u1.isDefeated()) {
                return new EngagementReport(u1,u2,SkirmishResult.U1_DEFEAT, engageCounter);
            }
            else if (u2.isDefeated()){
                return new EngagementReport(u1,u2,SkirmishResult.U2_DEFEAT, engageCounter);
            }
            else {
                engageCounter += 1;
            }
        }

    }

    //calculates the chances of the unit breaking
    public static double breakChances(Unit unit, Unit enemy, int casualtiesUnit, int casualtiesEnemy) {
        double base = clamp(0, 100 - unit.getMorale()*10,100);
        double addition =
                ((double)casualtiesUnit / unit.getNumTroops()) /
                ((double)(Math.max(casualtiesEnemy, 1)) / enemy.getNumTroops()) *
                        10.0;
        return clamp(5, base+addition, 100) / 100;
    }

    //calculates the chances of the unit fleeing
    public static double fleeChances(Unit fleeing, Unit pursuing) {
        return (clamp(10, 50 + 10.0 * (fleeing.getSpeed() - pursuing.getSpeed()), 100))/100;
    }
}
