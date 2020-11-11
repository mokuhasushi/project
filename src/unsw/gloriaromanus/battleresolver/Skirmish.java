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

    /**
     * Constructor instantiates engagement with the right instance
     * Implementing the strategy pattern
     * @param u1 first soldier
     * @param u2 second soldier
     */
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

    /**
     * Constructor instantiates engagement with the right instance
     * Implementing the strategy pattern
     * Firing changes to reporter (Observer pattern)
     * @param u1 Soldier
     * @param u2 Soldier
     * @param reporter Observer
     */
    public Skirmish (Soldier u1, Soldier u2, PropertyChangeListener reporter) {
        this.u1 = u1;
        this.u2 = u2;
        rng = new Random();
        support.addPropertyChangeListener(reporter);

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

    /**
     * Solves the skirmish between two units
     * @return a SkirmishReport, containing the units, the result, and the engagement counter
     */
    public SkirmishReport solve () {
        support.firePropertyChange("battlef1f2", null, "%s's "+u1.getName()+" engages %s's "+u2.getName());
        while (true) {
            if (u1.isBroken() && !u2.isBroken()) {
                if (rng.nextDouble() < fleeChances(u1, u2)) {
                    support.firePropertyChange("battlef1", null, "%s's " + u1.getName() + " flees the battle!");
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
                    support.firePropertyChange("battlef1", null, "%s " + u1.getName() +
                            " suffers " + cas[0] + " casualties while trying to escape!");
                }
            } else if (!u1.isBroken() && u2.isBroken()) {
                if (rng.nextDouble() < fleeChances(u2, u1)) {
                    support.firePropertyChange("battlef2", null, "%s's " + u2.getName() + " flees the battle!");
                    return new SkirmishReport(u1, u2, SkirmishResult.U2_FLEE, engageCounter);
                } else {
                    int[] cas = engagement.casualties(u1, u2);
                    u2.removeTroops(cas[1]);
                    support.firePropertyChange("battlef2", null, "%s's " + u2.getName() +
                            " suffers " + cas[1] + " casualties while trying to escape!");
                }
            } else if (u1.isBroken() && u2.isBroken()) {
                support.firePropertyChange("message", null, "Both units flee the battle!");
                return new SkirmishReport(u1, u2, SkirmishResult.DRAW_FLED, engageCounter);
            } else {
                int[] casualties = engagement.casualties(u1, u2);//TODO check specs
                support.firePropertyChange("battlef1f2", null, "%s's "+u1.getName()+" suffered "+casualties[0]+
                        " casualties, the %s's "+u2.getName()+" "+casualties[1]);
                if (breakChances(u1, u2, casualties[0], casualties[1]) > rng.nextDouble()) {
                    u1.setBroken(true);
                    support.firePropertyChange("battlef1", null, "%s's " + u1.getName() + " is routing!");
                }
                if (breakChances(u2, u1, casualties[1], casualties[0]) > rng.nextDouble()){
                    u2.setBroken(true);
                    support.firePropertyChange("battlef2", null, "%s's " + u2.getName() + " is routing!");
                }
                u1.removeTroops(casualties[0]);
                u2.removeTroops(casualties[1]);
            }
            if (u1.isDefeated() && u2.isDefeated()) {
                support.firePropertyChange("message", null, "Both units are defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.DRAW_DEFEATED, engageCounter);
            }
            else if (u1.isDefeated()) {
                support.firePropertyChange("battlef1", null, "%s's "+u1.getName()+" is defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.U1_DEFEAT, engageCounter);
            }
            else if (u2.isDefeated()){
                support.firePropertyChange("battlef2", null, "%s's "+u2.getName()+ "is defeated!");
                return new SkirmishReport(u1,u2,SkirmishResult.U2_DEFEAT, engageCounter);
            }
            else {
                engageCounter += 1;
            }
        }

    }

    /**
     * Calculates the chances of the unit soldier breaking
     * @param soldier unit to break
     * @param enemy enemy unit
     * @param casualtiesUnit the casualties of the unit, to calculate chances
     * @param casualtiesEnemy the casualties of the enemy
     * @return a double 0.05 &lt;= x &lt;= 1, containing the chances
     */
    public static double breakChances(Soldier soldier, Soldier enemy, int casualtiesUnit, int casualtiesEnemy) {
        int base = clamp(0, 100 - soldier.getMorale()*10,100);
        double addition =
                ((double)casualtiesUnit / soldier.getNumTroops()) /
                        ((double)(Math.max(casualtiesEnemy, 1)) / enemy.getNumTroops()) *
                        10.0;
        return clamp(5, base+addition, 100) / 100;
    }

    /**
     * Calculates the chances of the unit fleeing
     * @param fleeing Soldier
     * @param pursuing Soldier
     * @return a double 0.1 &lt;= x &lt;= 100
     */
    public static double fleeChances(Soldier fleeing, Soldier pursuing) {
        return (clamp(10, 50 + 10.0 * (fleeing.getSpeed() - pursuing.getSpeed()), 100))/100;
    }
}
