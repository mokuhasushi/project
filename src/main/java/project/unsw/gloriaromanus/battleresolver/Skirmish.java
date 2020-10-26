package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

public interface Skirmish {
    public SkirmishResult solve (Unit u1, Unit u2);
    public int damageDealt(Unit attacker, Unit defender);

    static double breakChances(Unit u1, Unit u2, int casualties1, int casualties2) {
        double base = 100 - u1.getMorale()*10;
        double addition =
                ((double)casualties1 / u1.getNumTroops()) /
                ((double)casualties2 / u2.getNumTroops()) *
                        10.0;
        return base + addition;
    }
}
