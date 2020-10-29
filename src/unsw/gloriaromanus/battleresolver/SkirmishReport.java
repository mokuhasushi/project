package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Soldier;

public class SkirmishReport {
    private final Soldier u1;
    private final Soldier u2;
    private final SkirmishResult result;
    private final int numEngagements;

    public Soldier getU1() {
        return u1;
    }

    public Soldier getU2() {
        return u2;
    }

    public SkirmishReport(Soldier u1, Soldier u2, SkirmishResult result, int numEngagements) {
        this.u1 = u1;
        this.u2 = u2;
        this.result = result;
        this.numEngagements = numEngagements;
    }

    public SkirmishResult getResult() {
        return result;
    }

    public int getNumEngagements() {
        return numEngagements;
    }
}
