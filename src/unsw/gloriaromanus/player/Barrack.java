package unsw.gloriaromanus.player;

import unsw.gloriaromanus.units.SoldierFactory;
import unsw.gloriaromanus.units.Soldier;

public class Barrack {
    private int turnToSlot1 = 0;
    private int turnToSlot2 = 0;

    private Soldier soldierSlot1 = null;
    private Soldier soldierSlot2 = null;

    private SoldierFactory soldierFactory;

    public Barrack () {
        this.soldierFactory = new SoldierFactory();
    }
    public Barrack (SoldierFactory sf) {
        this.soldierFactory = sf;
    }

    public boolean createSoldier(String soldier) {
        if (soldierSlot1 == null || turnToSlot1 <= 0) {
            soldierSlot1 = soldierFactory.createSoldier(soldier);
            turnToSlot1 = soldierSlot1.getTrainingTime();
            return true;
        }
        else if (soldierSlot2 == null || turnToSlot2 <= 0) {
            soldierSlot2 = soldierFactory.createSoldier(soldier);
            turnToSlot2 = soldierSlot2.getTrainingTime();
            return true;
        }
        return false;
    }

    public int getTurnToSlot1() {
        return turnToSlot1;
    }

    public int getTurnToSlot2() {
        return turnToSlot2;
    }

    public void turnPassed() {
        this.turnToSlot1 = turnToSlot1 - 1;
        this.turnToSlot2 = turnToSlot2 - 1;
    }

    public Soldier getSoldierSlot1() {
        Soldier s = soldierSlot1;
        if (turnToSlot1 == 0){
            soldierSlot1 = null;
            return s;}
        return null;
    }

    public Soldier getSoldierSlot2() {
        Soldier s = soldierSlot2;
        if (turnToSlot2 == 0){
            soldierSlot2 = null;
            return s;}
        return null;
    }
}
