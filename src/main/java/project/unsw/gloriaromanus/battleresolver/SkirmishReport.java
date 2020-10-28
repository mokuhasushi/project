package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Soldier;

public class SkirmishReport {
    private Soldier u1;
    private Soldier u2;
    private SkirmishResult result;
    private int numEngagements;

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

    public void setU1(Soldier u1) {
        this.u1 = u1;
    }

    public void setU2(Soldier u2) {
        this.u2 = u2;
    }

    public void setResult(SkirmishResult result) {
        this.result = result;
    }

    public void setNumEngagements(int numEngagements) {
        this.numEngagements = numEngagements;
    }

}
