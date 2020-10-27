package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Unit;

public class SkirmishReport {
    private Unit u1;
    private Unit u2;
    private SkirmishResult result;
    private int numEngagements;

    public Unit getU1() {
        return u1;
    }

    public Unit getU2() {
        return u2;
    }

    public SkirmishReport(Unit u1, Unit u2, SkirmishResult result, int numEngagements) {
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

    public void setU1(Unit u1) {
        this.u1 = u1;
    }

    public void setU2(Unit u2) {
        this.u2 = u2;
    }

    public void setResult(SkirmishResult result) {
        this.result = result;
    }

    public void setNumEngagements(int numEngagements) {
        this.numEngagements = numEngagements;
    }

}
