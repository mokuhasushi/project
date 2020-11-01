package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Soldier;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import static unsw.gloriaromanus.Utils.clamp;

/*
Skirmish Interface
Provides some static methods common to every instance.
The idea is to apply a Strategy Pattern to differentiate between melee and range
 */
public class Skirmish {
    private final Soldier u1;
    private final Soldier u2;
    private final Engagement engagement;
    private int engageCounter = 1;
    private final Random rng;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Constructor instantiates engagement with the right instance
    // Strategy pattern
    public Skirmish (Soldier u1, Soldier u2) {
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
    public Skirmish (Soldier u1, Soldier u2, PropertyChangeListener textReport) {
        this.u1 = u1;
        this.u2 = u2;
        rng = new Random();
        support.addPropertyChangeListener(textReport);

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
    public Skirmish (Soldier u1, Soldier u2, Random rng, Engagement eng) {
        this.u1 = u1;
        this.u2 = u2;
        this.rng = rng;
        this.engagement = eng;
    }

    public void addPropertyChangeListener (PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    //solves the engagement between two units
    public SkirmishReport solve () {
        support.firePropertyChange("", null, "Your "+u1.getName()+" engages enemy "+u2.getName());
        while (true) {
            if (u1.isBroken() && !u2.isBroken()) {
                if (rng.nextDouble() < fleeChances(u1, u2)) {
                    support.firePropertyChange("", null, "Your " + u1.getName() + " flees the battle!");
                    return new SkirmishReport(u1, u2, SkirmishResult.U1_FLEE, engageCounter);
                } else {
                    /*
                    If the broken unit doesn't route, it will suffer casualties without
                    damaging the other unit.
                    engagement.casualties() returns an Array of size 2, the first entry
                    relative to casualties for u1, the second for u2
                     */
                    int[] cas = engagement.casualties(u1, u2);
                    u1.removeTroops(cas[0]);
                    support.firePropertyChange("", null, "Your " + u1.getName() +
                            " suffers " + cas[0] + " casualties while trying to escape!");
                }
            } else if (!u1.isBroken() && u2.isBroken()) {
                if (rng.nextDouble() < fleeChances(u2, u1)) {
                    support.firePropertyChange("", null, "Enemy " + u2.getName() + " flees the battle!");
                    return new SkirmishReport(u1, u2, SkirmishResult.U2_FLEE, engageCounter);
                } else {
                    int[] cas = engagement.casualties(u1, u2);
                    u2.removeTroops(cas[1]);
                    support.firePropertyChange("", null, "Enemy " + u2.getName() +
                            " suffers " + cas[1] + " casualties while trying to escape!");
                }
            } else if (u1.isBroken() && u2.isBroken()) {
                support.firePropertyChange("", null, "Both units flee the battle!");
                return new SkirmishReport(u1, u2, SkirmishResult.DRAW_FLED, engageCounter);
            } else {
                int[] casualties = engagement.casualties(u1, u2);//TODO check specs
                support.firePropertyChange("", null, "Your "+u1.getName()+" suffered "+casualties[0]+
                        " casualties, the enemy "+u2.getName()+" "+casualties[1]);
                if (breakChances(u1, u2, casualties[0], casualties[1]) > rng.nextDouble()) {
                    u1.setBroken(true);
                    support.firePropertyChange("", null, "Your " + u1.getName() + " is routing!");
                }
                if (breakChances(u2, u1, casualties[1], casualties[0]) > rng.nextDouble()){
                    u2.setBroken(true);
                    support.firePropertyChange("", null, "Enemy " + u2.getName() + " is routing!");
                }
                u1.removeTroops(casualties[0]);
                u2.removeTroops(casualties[1]);
            }
            if (u1.isDefeated() && u2.isDefeated()) {
                // From the spec is not clear, I assume both defeated is a draw
                support.firePropertyChange("", null, "Both units are defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.DRAW_DEFEATED, engageCounter);
            }
            else if (u1.isDefeated()) {
                support.firePropertyChange("", null, "Your "+u1.getName()+" is defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.U1_DEFEAT, engageCounter);
            }
            else if (u2.isDefeated()){
                support.firePropertyChange("", null, "Enemy "+u2.getName()+ "is defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.U2_DEFEAT, engageCounter);
            }
            else {
                engageCounter += 1;
            }
        }

    }

    //calculates the chances of the unit breaking
    public static double breakChances(Soldier soldier, Soldier enemy, int casualtiesUnit, int casualtiesEnemy) {
        int base = clamp(0, 100 - soldier.getMorale()*10,100);
        double addition =
                ((double)casualtiesUnit / soldier.getNumTroops()) /
                        ((double)(Math.max(casualtiesEnemy, 1)) / enemy.getNumTroops()) *
                        10.0;
        return clamp(5, base+addition, 100) / 100;
    }

    //calculates the chances of the unit fleeing
    public static double fleeChances(Soldier fleeing, Soldier pursuing) {
        return (clamp(10, 50 + 10.0 * (fleeing.getSpeed() - pursuing.getSpeed()), 100))/100;
    }
}
